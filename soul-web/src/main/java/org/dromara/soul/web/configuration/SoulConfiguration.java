/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.dromara.soul.web.configuration;

import org.dromara.soul.web.cache.LocalCacheManager;
import org.dromara.soul.web.cache.UpstreamCacheManager;
import org.dromara.soul.web.config.SoulConfig;
import org.dromara.soul.web.disruptor.publisher.SoulEventPublisher;
import org.dromara.soul.web.filter.BodyWebFilter;
import org.dromara.soul.web.filter.ParamWebFilter;
import org.dromara.soul.web.filter.TimeWebFilter;
import org.dromara.soul.web.filter.WebSocketWebFilter;
import org.dromara.soul.web.handler.SoulWebHandler;
import org.dromara.soul.web.influxdb.service.InfluxDbService;
import org.dromara.soul.web.plugin.SoulPlugin;
import org.dromara.soul.web.plugin.after.MonitorPlugin;
import org.dromara.soul.web.plugin.after.ResponsePlugin;
import org.dromara.soul.web.plugin.before.GlobalPlugin;
import org.dromara.soul.web.plugin.before.SignPlugin;
import org.dromara.soul.web.plugin.before.WafPlugin;
import org.dromara.soul.web.plugin.function.DividePlugin;
import org.dromara.soul.web.plugin.function.RateLimiterPlugin;
import org.dromara.soul.web.plugin.function.RewritePlugin;
import org.dromara.soul.web.plugin.function.WebSocketPlugin;
import org.dromara.soul.web.plugin.ratelimter.RedisRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.WebFilter;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * fixme 注意 ！！！ 这边是整个插件的配置入口，
 * 如果需要自定义插件，需要这边引入自定义的插件
 *
 * 如果你的其他包都在使用了@SpringBootApplication注解的main
 * app所在的包及其下级包，则你什么都不用做，SpringBoot会自动帮你把其他包都扫描了
 * 如果你有一些bean所在的包，不在main
 * app的包及其下级包，那么你需要手动加上@ComponentScan注解并指定那个bean所在的包
 *
 * SoulConfiguration.
 *
 * @author xiaoyu(Myth)
 */
@Configuration
@ComponentScan("org.dromara.soul")
@Import(value = {DubboConfiguration.class, LocalCacheConfiguration.class, ErrorHandlerConfiguration.class})
public class SoulConfiguration {

    private final LocalCacheManager localCacheManager;

    private final UpstreamCacheManager upstreamCacheManager;

    /**
     * Instantiates a new Soul configuration.
     *
     * @param localCacheManager    the local cache manager
     * @param upstreamCacheManager the upstream cache manager
     */
    @Autowired(required = false)
    public SoulConfiguration(final LocalCacheManager localCacheManager,
                             final UpstreamCacheManager upstreamCacheManager) {
        this.localCacheManager = localCacheManager;
        this.upstreamCacheManager = upstreamCacheManager;
    }

    /**
     * init global plugin.
     *
     * @return {@linkplain GlobalPlugin}
     */
    @Bean
    public SoulPlugin globalPlugin() {
        return new GlobalPlugin();
    }


    /**
     * init sign plugin.
     *
     * @return {@linkplain SignPlugin}
     */
    @Bean
    public SoulPlugin signPlugin() {
        return new SignPlugin(localCacheManager);
    }

    /**
     * init waf plugin.
     *
     * @return {@linkplain WafPlugin}
     */
    @Bean
    public SoulPlugin wafPlugin() {
        return new WafPlugin(localCacheManager);
    }


    /**
     * init rateLimiterPlugin.
     *
     * @return {@linkplain RateLimiterPlugin}
     */
    @Bean
    public SoulPlugin rateLimiterPlugin() {
        return new RateLimiterPlugin(localCacheManager, redisRateLimiter());
    }


    /**
     * Redis rate limiter redis rate limiter.
     *
     * @return the redis rate limiter
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter();
    }

    /**
     * init rewritePlugin.
     *
     * @return {@linkplain RewritePlugin}
     */
    @Bean
    public SoulPlugin rewritePlugin() {
        return new RewritePlugin(localCacheManager);
    }

    /**
     * init dividePlugin.
     *
     * @return {@linkplain DividePlugin}
     */
    @Bean
    public SoulPlugin dividePlugin() {
        return new DividePlugin(localCacheManager, upstreamCacheManager);
    }

    /**
     * Web socket plugin web socket plugin.
     *
     * @param webSocketClient  the web socket client
     * @param webSocketService the web socket service
     * @return the web socket plugin
     */
    @Bean
    public WebSocketPlugin webSocketPlugin(final WebSocketClient webSocketClient,
                                           final WebSocketService webSocketService) {
        return new WebSocketPlugin(localCacheManager, upstreamCacheManager, webSocketClient, webSocketService);
    }

    /**
     * Influx db service influx db service.
     *
     * @return the influx db service
     */
    @Bean
    public InfluxDbService influxDbService() {
        return new InfluxDbService();
    }

    /**
     * Soul event publisher soul event publisher.
     *
     * @param influxDbService the influx db service
     * @return the soul event publisher
     */
    @Bean
    public SoulEventPublisher soulEventPublisher(InfluxDbService influxDbService) {
        return new SoulEventPublisher(influxDbService);
    }

    /**
     * Monitor plugin soul plugin.
     *
     * @param soulEventPublisher the soul event publisher
     * @return the soul plugin
     */
    @Bean
    public SoulPlugin monitorPlugin(SoulEventPublisher soulEventPublisher) {
        return new MonitorPlugin(soulEventPublisher, localCacheManager);
    }

    /**
     * init responsePlugin.
     *
     * @return {@linkplain ResponsePlugin}
     */
    @Bean
    public SoulPlugin responsePlugin() {
        return new ResponsePlugin();
    }

    /**
     * init SoulWebHandler.
     *
     * fixme 这边的soulPlugin是哪里 扫描的？ 能把前面的plugin的bean 组装成 List<SoulPlugin> 吗？
     *
     * @param plugins this plugins is All impl SoulPlugin.
     * @return {@linkplain SoulWebHandler}
     */
    @Bean("webHandler")
    public SoulWebHandler soulWebHandler(final List<SoulPlugin> plugins) {
        final List<SoulPlugin> soulPlugins = plugins.stream()
                .sorted((m, n) -> {
                    if (m.pluginType().equals(n.pluginType())) {
                        return m.getOrder() - n.getOrder();
                    } else {
                        return m.pluginType().getName().compareTo(n.pluginType().getName());
                    }
                }).collect(Collectors.toList());
        return new SoulWebHandler(soulPlugins);
    }

    /**
     * Body web filter web filter.
     *
     * @return the web filter
     */
    @Bean
    @Order(-1)
    public WebFilter bodyWebFilter() {
        return new BodyWebFilter();
    }


    /**
     * Param web filter web filter.
     *
     * @return the web filter
     */
    @Bean
    @Order(1)
    public WebFilter paramWebFilter() {
        return new ParamWebFilter();
    }

    /**
     * init time web filter.
     *
     * @param soulConfig the soul config
     * @return {@linkplain TimeWebFilter}
     */
    @Bean
    @Order(2)
    @ConditionalOnProperty(name = "soul.filterTimeEnable", matchIfMissing = true)
    public WebFilter timeWebFilter(final SoulConfig soulConfig) {
        return new TimeWebFilter(soulConfig);
    }


    /**
     * Web socket web filter web filter.
     *
     * @return the web filter
     */
    @Bean
    @Order(2)
    public WebFilter webSocketWebFilter() {
        return new WebSocketWebFilter();
    }


    /**
     * Reactor netty web socket client reactor netty web socket client.
     *
     * @return the reactor netty web socket client
     */
    @Bean
    public ReactorNettyWebSocketClient reactorNettyWebSocketClient() {
        return new ReactorNettyWebSocketClient();
    }

    /**
     * Web socket service web socket service.
     *
     * @return the web socket service
     */
    @Bean
    public WebSocketService webSocketService() {
        return new HandshakeWebSocketService();
    }

}
