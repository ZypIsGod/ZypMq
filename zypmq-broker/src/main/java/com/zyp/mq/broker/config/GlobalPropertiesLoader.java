package com.zyp.mq.broker.config;

import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.constants.BrokerConstants;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */
public class GlobalPropertiesLoader {

    private GlobalProperties globalProperties;

    public void loadProperties() {
        // 这里可以从配置文件、环境变量等多种来源加载全局配置
        this.globalProperties = new GlobalProperties();
        String property = System.getProperty(BrokerConstants.BROKER_HOME_ENV);
        if (property == null) {
            throw new IllegalStateException("Environment variable " + BrokerConstants.BROKER_HOME_ENV + " is not set");
        }
        globalProperties.setZypMqHome(property);
        CommonCache.globalProperties = globalProperties;
    }


}
