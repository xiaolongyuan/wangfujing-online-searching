package com.wfj.search.online.index.operation;

import com.wfj.platform.util.analysis.Timer;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.service.IIndexConfigService;
import com.wfj.search.online.index.util.ExecutorServiceFactory;
import com.wfj.search.util.record.pojo.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 分类缓存预热操作
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
@Component("categoryCacheWarmUpOperation")
public class CategoryCacheWarmUpOperation implements IOperation<Void> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean running = false;
    @Autowired
    private IPcmRequester pcmRequester;
    @Autowired
    private IIndexConfigService indexConfigService;

    @Override
    public Void operate(Operation operation) throws Exception {
        try {
            running = true;
            String cid = "0";
            Timer timer = new Timer();
            timer.start();
            AtomicLong sum = new AtomicLong(0);
            this.warmUpSubCategories(cid, sum);
            timer.stop();
            Duration duration = Duration.between(timer.getStartTime(), timer.lastStop().getStopTime());
            logger.info("warm-up {} categories finish, cost {}", sum.get(), duration.toString());
            return null;
        } finally {
            running = false;
        }
    }

    private void warmUpSubCategories(String cid, AtomicLong sum) throws RequestException {
        List<String> subIds = pcmRequester.listSubCategories(cid);
        if (subIds != null && !subIds.isEmpty()) {
            int threads = this.indexConfigService.getWarmUpFetchThreads();
            final AtomicReference<Throwable> tracker = new AtomicReference<>();
            ExecutorService threadPool = ExecutorServiceFactory.create("warm-up-category", threads,
                    Thread.currentThread(), tracker);
            CompletionService<Void> completionService = new ExecutorCompletionService<>(threadPool);
            for (String subId : subIds) {
                completionService.submit(() -> {
                    Timer timer1 = new Timer();
                    timer1.start();
                    try {
                        pcmRequester.directGetCategoryInfo(subId);
                    sum.incrementAndGet();
                    } catch (RequestException e) {
                        logger.warn("warm-up category[{}] failed", subId);
                    }
                    Duration stop = timer1.stop();
                    logger.debug("category[{}] warm-up, cost {}", subId, stop.toString());
                }, null);
            }
            //noinspection Duplicates
            try {
                for (int i = 0; i < subIds.size(); i++) {
                    completionService.take();
                }
            } catch (InterruptedException e) {
                Throwable throwable = tracker.get();
                throw new RuntimeException(e.toString(), throwable);
            }
            for (String subId : subIds) {
                warmUpSubCategories(subId, sum);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
