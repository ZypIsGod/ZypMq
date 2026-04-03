package com.zyp.mq.broker.model;

import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.constants.BrokerConstants;

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

    public void loadFileInMMap(String topicName, int startOffset, int mappendSize) throws IOException {

        String filePath = getLatestCommitLogFile(topicName);
        file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + filePath);
        }
        fileChannel = new RandomAccessFile(file, "rw").getChannel();
        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappendSize);
    }

    private String getLatestCommitLogFile(String topicName) {
        TopicModel topicModel = CommonCache.topicModelMap.get(topicName);
        if (topicModel == null) {
            throw new IllegalStateException("topic not found: " + topicName);
        }
        CommitLogModel commitLogModel = topicModel.getCommitLog();
        long diff = commitLogModel.getOffsetLimit() - commitLogModel.getOffset();
        String fileName = null;
        if (diff == 0) {
            //写满了
            fileName = this.createNewCommitLogFile();

        } else if (diff > 0) {
            fileName = commitLogModel.getFileName();
        }
        String bathMqHome = CommonCache.globalProperties.getZypMqHome();
        String brokerPath = BrokerConstants.BROKER_PATH;
        return bathMqHome + brokerPath + topicName + "/" + fileName;
    }

    private String createNewCommitLogFile() {

        return null;
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


    public void writeContent(byte[] content) {
        writeContent(content, false);
    }

    public void writeContent(byte[] content, boolean force) {

        ByteBuffer slice = mappedByteBuffer.slice();
        slice.position(111);
        //追加写入
        slice.put(content);
        mappedByteBuffer.put(content);
        if (force) {
            mappedByteBuffer.force();
        }
    }

    public void clear() {
        mappedByteBuffer.clear();
    }
}
