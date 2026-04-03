package com.zyp.mq.broker;

import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.config.GlobalPropertiesLoader;
import com.zyp.mq.broker.config.TopicInfoLoader;
import com.zyp.mq.broker.constants.BrokerConstants;
import com.zyp.mq.broker.core.MessageAppendHandler;
import com.zyp.mq.broker.model.TopicModel;

import java.io.IOException;
import java.util.List;

/**
 * @Date:2026/4/3
 * @Author：zyp
 * @Description:
 */
public class BrokerStartUp  {

    private GlobalPropertiesLoader globalPropertiesLoader;
    private TopicInfoLoader topicInfoLoader;

    private MessageAppendHandler messageAppendHandler;

    private void initProperties() throws IOException {
        // 初始化配置文件
        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        topicInfoLoader = new TopicInfoLoader();
        topicInfoLoader.loadProperties();
        messageAppendHandler = new MessageAppendHandler();
        //这里面存的topic和队列信息。
        List<TopicModel> topicModelList = CommonCache.topicModelList;
         for(TopicModel topicModel : topicModelList) {
             String bathMqHome = CommonCache.globalProperties.getZypMqHome();
             String brokerPath = BrokerConstants.BROKER_PATH;
             String filePath = bathMqHome + brokerPath + topicModel.getTopic()+"00000000";
             messageAppendHandler.prepareMMpLoading(filePath,topicModel.getTopic());

         }

    }
    public static void main(String[] args) {

    }
}
