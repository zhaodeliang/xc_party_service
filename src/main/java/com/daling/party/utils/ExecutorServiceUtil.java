package com.daling.party.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by wangheming on 2019/3/29.
 */
public class ExecutorServiceUtil {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static Logger logger = LoggerFactory.getLogger("ExecutorServiceUtil");

    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5000);

    private static final ExecutorService executor = new ThreadPoolExecutor(CPU_COUNT + 1, CPU_COUNT * 2 + 1, 60L, TimeUnit.SECONDS, queue,
            new RejectedExecutionHandler() {
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    try {
                        executor.getQueue().put(r);
                    } catch (Exception e) {
                        logger.error("线程池入队异常", e);
                    }
                }
            });

    public static ExecutorService newExecutorInstance() {
        return executor;
    }
}