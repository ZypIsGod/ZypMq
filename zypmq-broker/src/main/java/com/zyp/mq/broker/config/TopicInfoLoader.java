package com.zyp.mq.broker.config;

import com.alibaba.fastjson.JSON;
import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.model.TopicModel;
import com.zyp.mq.broker.utils.FileContentReaderUtils;
import io.netty.util.internal.StringUtil;

import java.util.List;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */
public class TopicInfoLoader {

    private TopicModel topicModel;

    public void loadProperties() {
        GlobalProperties globalPropertiesLoader = CommonCache.globalProperties;
        String bathPath = globalPropertiesLoader.getZypMqHome();
        if (StringUtil.isNullOrEmpty(bathPath)) {
            throw new IllegalStateException("ZYP_MQ_HOME is not set");
        }
        String topicJsonFilePath = bathPath+"/broker/config/ZypMq-topic.json";
        String topicJson = FileContentReaderUtils.readFromFile(topicJsonFilePath);

        CommonCache.topicModelList = JSON.parseArray(topicJson, TopicModel.class);

    }
}
