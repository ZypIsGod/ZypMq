package com.zyp.mq.broker.core;

import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.constants.BrokerConstants;
import com.zyp.mq.broker.model.CommitLogMessageModel;
import com.zyp.mq.broker.model.MMpFileModel;

import java.io.IOException;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */
public class CommitLogAppendHandler {

    private MMpFileModelManager mMpFileModelManager = new MMpFileModelManager();



    public void prepareMMpLoading(String topicName) throws IOException {

        this.mMpFileModelManager = new MMpFileModelManager();
        MMpFileModel mMpFileModel = new MMpFileModel();
        mMpFileModel.loadFileInMMap(topicName, 0, 1024 * 1024 * 100);
        mMpFileModelManager.put(topicName, mMpFileModel);
    }

    public void appendMsg(String topic, byte[] content) throws IOException {
        MMpFileModel mMpFileModel = mMpFileModelManager.get(topic);
        if (mMpFileModel == null) {
            throw new IllegalStateException("topic not exist: " + topic);
        }
        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
        commitLogMessageModel.setContent(content);
        commitLogMessageModel.setSize(content.length);

        mMpFileModel.writeContent(commitLogMessageModel);
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

        CommitLogAppendHandler commitLogAppendHandler = new CommitLogAppendHandler();
        commitLogAppendHandler.appendMsg("pay_topic", "hello zyp111".getBytes());

        String s = commitLogAppendHandler.readMsg("pay_topic");
        System.out.println(s);

    }
}
