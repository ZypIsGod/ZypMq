package com.zyp.mq.broker.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Date:2026/4/6
 * @Author：zyp
 * @Description:
 */
public class CommonThreadPoolConfig {

    //用于异步刷盘的线程池
    public static ThreadPoolExecutor refreshZypMqTopicExecutor = new ThreadPoolExecutor(1,
            1,
            30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("refreshZypMqTopicExecutor");
            return thread;
        }
    });
}
