package com.zyp.mq.broker.model;

import java.util.List;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */
public class TopicModel {

    private String topicName;

    private List<TopicQueueModel> topicQueueList;

    private String createAt;

    private String updateAt;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<TopicQueueModel> getTopicQueueList() {
        return topicQueueList;
    }

    public void setTopicQueueList(List<TopicQueueModel> topicQueueList) {
        this.topicQueueList = topicQueueList;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
