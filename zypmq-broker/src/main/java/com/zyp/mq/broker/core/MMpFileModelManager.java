package com.zyp.mq.broker.core;

import com.zyp.mq.broker.model.MMpFileModel;

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
import java.util.HashMap;
import java.util.Map;

/**
 * @Date:2026/3/28
 * @Author：zyp
 * @Description:
 */
public class MMpFileModelManager {
    private Map<String, MMpFileModel> mMpFileModelMap = new HashMap<>();


    public void put(String topicName, MMpFileModel mMpFileModel) {
        mMpFileModelMap.put(topicName, mMpFileModel);
    }

    public MMpFileModel get(String topicName) {
        return mMpFileModelMap.get(topicName);
    }
}
