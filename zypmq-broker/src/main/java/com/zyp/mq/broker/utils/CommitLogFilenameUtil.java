package com.zyp.mq.broker.utils;

import com.zyp.mq.broker.cache.CommonCache;
import com.zyp.mq.broker.constants.BrokerConstants;

/**
 * @Date:2026/4/3
 * @Author：zyp
 * @Description:
 */
public class CommitLogFilenameUtil {


    /**
     * 构建第一份commitLog文件名称
     */
    public static String buildFirstCommitLogFileName() {
        return "00000000";
    }

    /**
     * 构建新的commitLog文件名称
     */
    public static String buildNewCommitLogFileName(String oldFileName) {
        if(oldFileName.length() != 8) {
            throw new IllegalArgumentException("invalid file name: " + oldFileName);
        }
        long oldFileNum = Long.parseLong(oldFileName);
        long newFileNum = oldFileNum + 1;
        String newFileName = String.valueOf(newFileNum);
        int newFileNameLen = newFileName.length();
        int needFullLen = 8 - newFileNameLen;
        if(needFullLen < 0) {
            throw new IllegalStateException("file num overflow: " + newFileNum);
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < needFullLen; i++) {
            stringBuffer.append("0");
        }
        stringBuffer.append(newFileName);
        return stringBuffer.toString();
    }

    public static String builCommitLogFilePath(String topicName, String newFileName){
        String bathMqHome = CommonCache.globalProperties.getZypMqHome();
        String brokerPath = BrokerConstants.BROKER_PATH;
        return bathMqHome + brokerPath + topicName + "/" + newFileName;
    }

    public static void main(String[] args) {

        String newFileName = buildNewCommitLogFileName("00000000");
        System.out.println(newFileName);
    }
}
