package com.wfj.search.online.index.util;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <br/>create at 15-12-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ExecutorServiceFactory {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceFactory.class);
    private static final AtomicLong threadId = new AtomicLong();
    private static final DecimalFormat df = new DecimalFormat("0000000000");

    private ExecutorServiceFactory() {
    }

    public static ExecutorService create(String prefix, int nThreads, Thread masterThread,
            AtomicReference<Throwable> tracker) {
        Validate.notNull(masterThread, "主线程对象不能为空");
        Validate.notNull(tracker, "异常收集器不能为空");
        return Executors.newFixedThreadPool(nThreads, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(prefix + "-" + df.format(threadId.incrementAndGet()));
            thread.setUncaughtExceptionHandler((t, e) -> {
                logger.error("产生未被捕捉异常, 线程名：{}", t.getName(), e);
                tracker.set(e);
                masterThread.interrupt();
            });
            return thread;
        });
    }
}
