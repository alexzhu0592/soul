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

package org.dromara.soul.web.balance.utils;

import org.dromara.soul.common.dto.convert.DivideUpstream;
import org.dromara.soul.web.balance.factory.LoadBalanceFactory;

import java.util.List;

/**
 * The type Load balance utils.
 *
 * @author xiaoyu(Myth)
 */
public class LoadBalanceUtils {

    /**
     * Selector divide upstream.
     * lb算法会被 divide 和 websocket的插件使用到
     *
     * @param upstreamList the upstream list
     * @param rule         the rule
     * @param ip           the ip
     * @return the divide upstream
     */
    public static DivideUpstream selector(final List<DivideUpstream> upstreamList, final String rule, final String ip) {
        return LoadBalanceFactory.of(rule).select(upstreamList, ip);
    }

}
