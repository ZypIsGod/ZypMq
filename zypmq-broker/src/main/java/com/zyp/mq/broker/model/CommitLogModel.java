package com.zyp.mq.broker.model;

/**
 * @Date:2026/4/3
 * @Author：zyp
 * @Description:
 */
public class CommitLogModel {

    private String fileName;

    private Long offset;

    private Long offsetLimit;

    public Long getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(Long offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }
}
