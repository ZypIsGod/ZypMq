package com.zyp.mq.broker.model;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */

public class TopicQueueModel {

    /**
     *       {
     *         "id": 1,
     *         "minOffset": 1111,
     *         "currentOffset": 1111,
     *         "maxOffset": 2222
     *       }
     */
    private Long id;

    private Integer minOffset;
    private Integer maxOffset;

    private Integer currentOffset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinOffset() {
        return minOffset;
    }

    public void setMinOffset(Integer minOffset) {
        this.minOffset = minOffset;
    }

    public Integer getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(Integer maxOffset) {
        this.maxOffset = maxOffset;
    }

    public Integer getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(Integer currentOffset) {
        this.currentOffset = currentOffset;
    }

    @Override
    public String toString() {
        return "TopicQueueModel{" +
                "id=" + id +
                ", minOffset=" + minOffset +
                ", maxOffset=" + maxOffset +
                ", currentOffset=" + currentOffset +
                '}';
    }
}
