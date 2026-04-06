package com.zyp.mq.broker.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Date:2026/4/3
 * @Author：zyp
 * @Description:
 */
public class CommitLogModel {

    private String fileName;

    private AtomicLong offset;

    private AtomicLong offsetLimit;

    public AtomicLong getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(AtomicLong offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public AtomicLong getOffset() {
        return offset;
    }

    public void setOffset(AtomicLong offset) {
        this.offset = offset;
    }

    public Long countDiff(){
        return this.getOffsetLimit().get() - this.getOffset().get();
    }
}
