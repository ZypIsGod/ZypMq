package com.zyp.mq.broker.model;

import com.zyp.mq.broker.utils.ByteConvertUtils;

/**
 * @Date:2026/4/4
 * @Author：zyp
 * @Description:
 */
public class CommitLogMessageModel {

    /**
     * 消息的体积大小，单位字节
     */
    private int size;

    /**
     * 消息的内容
     */
    private byte[] content;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] convertToByte() {
        byte[] bytes = ByteConvertUtils.intToBytes(this.getSize());
        byte[] content = this.getContent();
        byte[] mergeResultByte = new byte[bytes.length + content.length];
        int j = 0;
        for (int i = 0; i < bytes.length; i++, j++) {
            mergeResultByte[j] = bytes[i];
        }
        for (int i = 0; i < content.length; i++, j++) {
            mergeResultByte[j] = content[i];
        }
        return mergeResultByte;
    }
}
