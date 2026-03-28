package com.zyp.mq.broker.utils;

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
public class MMapUtils {

    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;

    public void loadFileInMMap(String filePath, int startOffset, int mappendSize) throws IOException {
        file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + filePath);
        }
        fileChannel = new RandomAccessFile(file, "rw").getChannel();
        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappendSize);
    }

    public byte[] readContent(int readOffset, int size) {
        mappedByteBuffer.position(readOffset);
        byte[] content = new byte[size];
        for(int i = 0; i < size; i++) {
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
        writeContent(content,false);
    }

    public void writeContent(byte[] content, boolean force) {
        mappedByteBuffer.put(content);
        if(force) {
            mappedByteBuffer.force();
        }
    }

    public void clear(){
        mappedByteBuffer.clear();
    }
}
