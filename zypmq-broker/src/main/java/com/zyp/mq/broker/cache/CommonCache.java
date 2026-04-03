package com.zyp.mq.broker.cache;

import com.zyp.mq.broker.config.GlobalProperties;
import com.zyp.mq.broker.config.GlobalPropertiesLoader;
import com.zyp.mq.broker.config.TopicInfoLoader;
import com.zyp.mq.broker.config.TopicInfoProperties;
import com.zyp.mq.broker.model.TopicModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description: 统一缓存对象
 */
public class CommonCache {

    public static GlobalProperties globalProperties = new GlobalProperties();
    public static TopicInfoProperties topicInfoProperties = new TopicInfoProperties();

    public static List<TopicModel> topicModelList = new ArrayList<>();


}
