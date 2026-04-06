package com.zyp.mq.broker.cache;

import com.zyp.mq.broker.config.GlobalProperties;
import com.zyp.mq.broker.config.GlobalPropertiesLoader;
import com.zyp.mq.broker.config.TopicInfoLoader;
import com.zyp.mq.broker.config.TopicInfoProperties;
import com.zyp.mq.broker.model.TopicModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description: 统一缓存对象
 */
public class CommonCache {

    public static GlobalProperties globalProperties = new GlobalProperties();
    public static TopicInfoProperties topicInfoProperties = new TopicInfoProperties();

    public static List<TopicModel> topicModelList = new ArrayList<>();


    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static TopicInfoProperties getTopicInfoProperties() {
        return topicInfoProperties;
    }

    public static void setTopicInfoProperties(TopicInfoProperties topicInfoProperties) {
        CommonCache.topicInfoProperties = topicInfoProperties;
    }

    public static Map<String, TopicModel> getTopicModelMap() {
        return topicModelList.stream().collect(Collectors.toMap(TopicModel::getTopic, topicModel -> topicModel));
    }

    public static void setTopicModelLis(List<TopicModel> setTopicModelList) {
        topicModelList = setTopicModelList;
    }

    public static List<TopicModel> getTopicModelList() {
        return topicModelList;
    }
}
