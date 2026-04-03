package com.zyp.mq.broker.core;

import java.io.IOException;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */
public class MessageAppendHandler {

    private MMpFileModelManager mMpFileModelManager = new MMpFileModelManager();



    public void prepareMMpLoading(String filePath, String topicName) throws IOException {
        this.mMpFileModelManager = new MMpFileModelManager();
        MMpFileModel mMpFileModel = new MMpFileModel();
        mMpFileModel.loadFileInMMap(filePath, 0, 1024 * 1024 * 100);
        mMpFileModelManager.put(topicName, mMpFileModel);
    }

    public void appendMsg(String topic, String content) {
        MMpFileModel mMpFileModel = mMpFileModelManager.get(topic);
        if (mMpFileModel == null) {
            throw new IllegalStateException("topic not exist: " + topic);
        }
        mMpFileModel.writeContent(content.getBytes());
    }

    public String readMsg(String topicName){
        MMpFileModel mMpFileModel = mMpFileModelManager.get(topicName);
        if (mMpFileModel == null) {
            throw new IllegalStateException("topic not exist: " + topicName);
        }
        byte[] bytes = mMpFileModel.readContent(0, 10);
        return new String(bytes);
    }

    public static void main(String[] args) throws IOException {

        MessageAppendHandler messageAppendHandler = new MessageAppendHandler();
        messageAppendHandler.appendMsg("pay_topic", "hello zyp111");

        String s = messageAppendHandler.readMsg("pay_topic");
        System.out.println(s);

    }
}
