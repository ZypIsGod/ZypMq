package com.zyp.mq.broker.model;

import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.constants.BrokerConstants;
import com.zyp.mq.broker.utils.ByteConvertUtils;
import com.zyp.mq.broker.utils.CommitLogFilenameUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @Date:2026/3/28
 * @Author：zyp
 * @Description:
 */
public class MMpFileModel {

    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;

    private String topicName;

    public void loadFileInMMap(String topicName, int startOffset, int mappendSize) throws IOException {
        this.topicName = topicName;
        String filePath = getLatestCommitLogFile(topicName);
        this.doMMap(filePath, startOffset, mappendSize);

    }

    private void doMMap(String filePath, int startOffset, int mappendSize) throws IOException {
        file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + filePath);
        }

        fileChannel = new RandomAccessFile(file, "rw").getChannel();
        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappendSize);
    }

    private String getLatestCommitLogFile(String topicName) {
        TopicModel topicModel = CommonCache.getTopicModelMap().get(topicName);
        if (topicModel == null) {
            throw new IllegalStateException("topic not found: " + topicName);
        }
        CommitLogModel commitLogModel = topicModel.getCommitLog();
        long diff = commitLogModel.countDiff();
        String fileName = null;
        if (diff == 0) {
            //写满了
            fileName = this.createNewCommitLogFile(topicName, commitLogModel);

        } else if (diff > 0) {
            fileName = commitLogModel.getFileName();
        }
        String bathMqHome = CommonCache.globalProperties.getZypMqHome();
        String brokerPath = BrokerConstants.BROKER_PATH;
        return bathMqHome + brokerPath + topicName + "/" + fileName;
    }

    private String createNewCommitLogFile(String topicName, CommitLogModel commitLog) {
        String bathMqHome = CommonCache.globalProperties.getZypMqHome();
        String brokerPath = BrokerConstants.BROKER_PATH;
        String newFilePath = bathMqHome + brokerPath + topicName + "/" + CommitLogFilenameUtil.buildNewCommitLogFileName(commitLog.getFileName());

        File newCommitLogFile = new File(newFilePath);
        try {
            newCommitLogFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newFilePath;
    }

    public byte[] readContent(int readOffset, int size) {
        mappedByteBuffer.position(readOffset);
        byte[] content = new byte[size];
        for (int i = 0; i < size; i++) {
            content[i] = mappedByteBuffer.get(readOffset + i);
        }
        return content;
    }

    public void clean() {
        if (mappedByteBuffer == null || !mappedByteBuffer.isDirect() || mappedByteBuffer.capacity() == 0) {
            return;
        }
        invoke(invoke(viewed(mappedByteBuffer), "cleaner"), "clean");
    }

    private Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    private Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    private ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null)
            return buffer;
        else
            return viewed(viewedBuffer);
    }


    public void writeContent(CommitLogMessageModel commitLogMessageModel) throws IOException {
        writeContent(commitLogMessageModel, false);
    }

    public void writeContent(CommitLogMessageModel commitLogMessageModel, boolean force) throws IOException {
        TopicModel topicModel = CommonCache.getTopicModelMap().get(topicName);
        if(topicModel == null) {
            throw new IllegalStateException("topic not found: " + topicName);
        }
        CommitLogModel commitLog = topicModel.getCommitLog();
        if(commitLog == null) {
            throw new IllegalStateException("commit log not found for topic: " + topicName);
        }
        this.checkCommitLogHasEnoughSpace(commitLogMessageModel);
        mappedByteBuffer.put(commitLogMessageModel.convertToByte());
        commitLog.getOffset().addAndGet(commitLogMessageModel.getSize());

        if (force) {
            mappedByteBuffer.force();
        }
    }

    private void checkCommitLogHasEnoughSpace(CommitLogMessageModel commitLogMessageModel) throws IOException {
        TopicModel topicModel = CommonCache.getTopicModelMap().get(this.topicName);
        CommitLogModel commitLog = topicModel.getCommitLog();
        long writeAbleOffsetNum = commitLog.countDiff();
        if (!(writeAbleOffsetNum >= commitLogMessageModel.getSize())) {
            //创建新的commitLog文件
            String newCommitLogFile = this.createNewCommitLogFile(this.topicName, commitLog);

            this.doMMap(newCommitLogFile, 0, BrokerConstants.COMMIT_LOG_FILE_SIZE);
        }
    }

    public void clear() {
        mappedByteBuffer.clear();
    }
}
