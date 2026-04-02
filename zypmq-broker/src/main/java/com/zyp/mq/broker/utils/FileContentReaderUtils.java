package com.zyp.mq.broker.utils;

import com.alibaba.fastjson.JSON;
import com.zyp.mq.broker.config.TopicInfo;
import com.zyp.mq.broker.model.TopicModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @Date:2026/4/1
 * @Author：zyp
 * @Description:
 */
public class FileContentReaderUtils {

    public static String readFromFile(String path) {
        try( BufferedReader in = new BufferedReader(new FileReader(path))) {
            StringBuffer stb = new StringBuffer();
            while (in.ready()) {
                stb.append(in.readLine());
            }
            return stb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String path = "/Users/didi/project/ZypMq/broker/config/ZypMq-topic.json";
        String content = readFromFile(path);

        List<TopicModel> topicModels = JSON.parseArray(content, TopicModel.class);

        System.out.println(topicModels);
    }
}
