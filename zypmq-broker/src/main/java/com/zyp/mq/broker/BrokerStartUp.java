package com.zyp.mq.broker;

import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.config.GlobalPropertiesLoader;
import com.zyp.mq.broker.config.TopicInfoLoader;
import com.zyp.mq.broker.constants.BrokerConstants;
import com.zyp.mq.broker.core.CommitLogAppendHandler;
import com.zyp.mq.broker.model.TopicModel;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @Date:2026/4/3
 * @Author：zyp
 * @Description:
 */
public class BrokerStartUp  {

    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static TopicInfoLoader topicInfoLoader;

    private static CommitLogAppendHandler commitLogAppendHandler;

    private static void initProperties() throws IOException {
        // 初始化配置文件
        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        topicInfoLoader = new TopicInfoLoader();
        topicInfoLoader.loadProperties();
        topicInfoLoader.startRefreshMqTopInfoTask();
        commitLogAppendHandler = new CommitLogAppendHandler();
        //这里面存的topic和队列信息。
        Collection<TopicModel> topicModelList = CommonCache.topicModelMap.values();
        for(TopicModel topicModel : topicModelList) {
             commitLogAppendHandler.prepareMMpLoading(topicModel.getTopic());
         }

    }
    public static void main(String[] args) throws IOException {
        initProperties();
        BrokerStartUp brokerStartUp = new BrokerStartUp();
        brokerStartUp.initProperties();

        commitLogAppendHandler.appendMsg("order_cancel_topic", "hello zyp111".getBytes());

        String s = commitLogAppendHandler.readMsg("order_cancel_topic");
        System.out.println(s);
    }
}
