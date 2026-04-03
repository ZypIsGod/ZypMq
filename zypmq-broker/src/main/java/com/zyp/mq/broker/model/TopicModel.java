package com.zyp.mq.broker.model;

import java.util.List;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */
public class TopicModel {

    private String topic;

    private List<TopicQueueModel> topicQueueList;

    private CommitLogModel commitLog;

    private String createAt;

    private String updateAt;

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
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

    public CommitLogModel getCommitLog() {
        return commitLog;
    }

    public void setCommitLog(CommitLogModel commitLog) {
        this.commitLog = commitLog;
    }

    @Override
    public String toString() {
        return "TopicModel{" +
                "topic='" + topic + '\'' +
                ", topicQueueList=" + topicQueueList +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                '}';
    }
}
