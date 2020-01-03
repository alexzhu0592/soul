# [Soul](https://dromara.org)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4367ffad5b434b7e8078b3a68cc6398d)](https://www.codacy.com/app/yu199195/soul?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Dromara/soul&amp;utm_campaign=Badge_Grade)
[![Total lines](https://tokei.rs/b1/github/Dromara/soul?category=lines)](https://github.com/Dromara/soul)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Dromara/soul/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/Dromara/soul.svg?branch=master)](https://travis-ci.org/Dromara/soul)
[![Maven Central](https://img.shields.io/maven-central/v/org.dromara/soul.svg?label=maven%20central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.dromara%22%20AND%soul)
[![QQ群](https://img.shields.io/badge/chat-on%20QQ-ff69b4.svg?style=flat-square)](https://shang.qq.com/wpa/qunwpa?idkey=03bbb6f74b3257989316c0a8cf07cec117314dbdfe4fa7a20870b298b7db2c3b)

### alex-src  
  alex-src分支在根据目前架构分析，需要单独维护一个管理后台，增加了运维的复杂度，打算把这一块
  直接集成到nacos上，nacos已经作为了常用的配置中心和服务注册发现的工具  
  刚刚发现有issue提到过nacos集成的事宜，作者也在3.x的版本中正在开发，拭目以待吧

### Reactive gateway based on webflux

# Architecture
 
 ![](https://yu199195.github.io/images/soul/soul-framework.png)  
  
# Execution Flow
 
 ![](https://yu199195.github.io/images/soul/soul-handler.png)
  
# Modules

 * soul-admin : Plug-in and other information configuration management background  
   soul管理后台：插件和其他配置信息的管理后台
 * soul-bootstrap : With the startup project, users can refer to  
   soul启动器：用户能够参考启动项目
 * soul-common :  Framework common class  
   soul-common模块：框架常用的类
 * soul-configuration : zookeeper configuration project  
   soul配置：zk配置项目
 * soul-spring-boot-starter : Support for the spring boot starter  
   soul的springboot starter：支持springboot
 * soul-web : Core processing packages include plug-ins, request routing and forwarding, and so on  
   soul的web：soul的主要处理流程，包括插件，请求转发和请求路径等等
 * soul-extend-demo : Demo of the extension point  
   soul的扩展demo
 * soul-test : the rpc test project  
   soul的rpc单元测试项目
# Features 特性

   * It provides plugins such as current limiting, fusing, forwarding, routing monitoring and so on.  
     提供了诸如限流，转发，路径监控等等的插件
   * Seamless docking with HTTP,Restful,websocket,dubbo and springcloud.  
     无侵入集成http、restful、websocket、dubbo和springcloud
   * Plug-in hot plug, users can customize the development.  
     支持热插拔。用户可以进行自定义开发
   * Selectors and rules are dynamically configured for flexible matching.  
     通过动态配置选择器和规则实现灵活的匹配
   * Support for cluster deployment.  
     支持集群部署
   * Support A/B test and grayscale publishing。  
     支持ab测试和灰度发布

# Plugin

 Whenever a request comes in ,Soul Execute all open plug-ins through the chain of responsibility.  
 当一个请求竟来的时候soul会执行一系列的责任链  
 Plugins are the heart of soul And plug-ins are extensible and hot-pluggable.  
 插件是soul的核心，插件是可扩展的和可热插拔的  
 Different plug-ins do different things   
 不同的插件做不同的事情  
 Of course, users can also customize plug-ins to meet their own needs.  
 当然，用户可以自定义插件去满足自己的要求   
 If you want to customize, see [plugin-extend](https://dromara.org/website/zh-cn/docs/soul/extend.html)
 

# Selector & rule 

  According to your HTTP request headers, selectors and rules are used to route your requests.  
  通过你http请求头，选择器和规则会用于路由你的请求  
  Selector is your first route, It is coarser grained, for example, at the module level.  
  选择器是你的第一个路由，在模块层面，是粗粒度的  
  Rule is your second route and what do you think your request should do,For example a method level in a module.  
  
  The selector and the rule match only once, and the match is returned. So the coarsest granularity should be sorted last.  
   
  
# Data Caching  & Data Sync
 
  All data is cached ConcurrentHashMap in the JVM So it's very fast.
  
  When the user is managing changes in the background,
  
  Soul dynamically updates the cache by listening to the zookeeper node, websocket push,http longPull.
  
  ![Data Sync](https://bestkobe.gitee.io/images/soul/soul-config-processor.png?_t=201908032316)
  
  ![Sync Flow](https://bestkobe.gitee.io/images/soul/config-strage-processor.png?_t=201908032339)
 
# Quick Start
 * get `soul-admin.jar`
 
```
> wget  https://yu199195.github.io/jar/soul-admin.jar
```

* start `soul-admin.jar`
```java
java -jar soul-admin.jar --spring.datasource.url="your mysql url"  
--spring.datasource.username='you username'  --spring.datasource.password='you password'
```
* visit : http://localhost:8887/index.html  username:admin  password :123456

* get `soul-bootstrap.jar`

```java
> wget  https://yu199195.github.io/jar/soul-bootstrap.jar
```

*  start `soul-bootstrap.jar`  

```xml
 java -jar soul-bootstrap.jar
```

# Prerequisite
 
   * JDK 1.8+
   
   * Mysql
   
# About & Document
  
   Soul Has been used in our production environment,Its performance and flexibility allow us to use up very cool.
   
   In double 11, we deployed 6 clusters, which supported a large volume of our business.
   
   If you want to use it, you can see [Document](https://dromara.org/website/zh-cn/docs/soul/soul.html)
        
# Stargazers over time

[![Stargazers over time](https://starchart.cc/Dromara/soul.svg)](https://starchart.cc/Dromara/soul)

# Videos

* [evn setup 01 ](http://www.iqiyi.com/w_19s6521605.html)

* [evn setup 02 ](http://www.iqiyi.com/w_19s65203ap.html)

* [source code debug](http://www.iqiyi.com/w_19s650tbol.html)

* [plugins](http://www.iqiyi.com/w_19s651zyo9.html)

# Support  

 [![芋道源码](http://www.iocoder.cn/images/common/erweima.jpg)](http://www.iocoder.cn/?from=soul) ![](https://yu199195.github.io/images/public.jpg)  ![](https://yu199195.github.io/images/soul-qq.png)   [![JetBrains](https://yu199195.github.io/images/jetbrains.svg)](https://www.jetbrains.com/?from=soul)
  
 
 