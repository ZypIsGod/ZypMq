package com.zyp.mq.broker.config;

import com.alibaba.fastjson.JSON;
import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.constants.BrokerConstants;
import com.zyp.mq.broker.model.TopicModel;
import com.zyp.mq.broker.utils.FileContentUtils;
import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */
public class TopicInfoLoader {

    private String filePath;

    public void loadProperties() {
        GlobalProperties globalPropertiesLoader = CommonCache.globalProperties;
        String bathPath = globalPropertiesLoader.getZypMqHome();
        if (StringUtil.isNullOrEmpty(bathPath)) {
            throw new IllegalStateException("ZYP_MQ_HOME is not set");
        }
        String topicJsonFilePath = bathPath + "/broker/config/ZypMq-topic.json";
        String topicJson = FileContentUtils.readFromFile(topicJsonFilePath);
        List<TopicModel> topicModels = JSON.parseArray(topicJson, TopicModel.class);
        CommonCache.setTopicModelLis(topicModels);

    }

    /**
     * 开启一个刷新内存到磁盘的任务
     */
    public void startRefreshMqTopInfoTask() {
        CommonThreadPoolConfig.refreshZypMqTopicExecutor.execute(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP);
                        List<TopicModel> topicModelList = CommonCache.getTopicModelList();
                        FileContentUtils.overWriteToFile(filePath,JSON.toJSONString(topicModelList));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } while (true);
            }
        });
    }
}
