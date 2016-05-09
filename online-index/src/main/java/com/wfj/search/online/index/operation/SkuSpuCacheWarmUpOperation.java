package com.wfj.search.online.index.operation;

import com.google.common.collect.Sets;
import com.wfj.search.online.common.pojo.ItemPojo;
import com.wfj.search.online.common.pojo.Page;
import com.wfj.search.online.common.pojo.SkuPojo;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.service.IIndexConfigService;
import com.wfj.search.online.index.util.ExecutorServiceFactory;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.utils.timer.Timer;
import com.wfj.search.utils.timer.TimerStop;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 品牌缓存预热操作
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
@Component("skuSpuCacheWarmUpOperation")
public class SkuSpuCacheWarmUpOperation implements IOperation<Void> {
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
            int itemCount = pcmRequester.countItems();
            Set<String> skuSet = Collections.synchronizedSet(Sets.newHashSetWithExpectedSize(itemCount));
            int fetchSize = indexConfigService.getWarmUpFetchSize();
            int pageSize = (itemCount + fetchSize) / fetchSize;
            Timer timer = new Timer();
            timer.start();
            int threads = this.indexConfigService.getWarmUpFetchThreads();
            final AtomicReference<Throwable> tracker = new AtomicReference<>();
            ExecutorService threadPool = ExecutorServiceFactory.create("warm-up-sku-spu-list-items", threads,
                    Thread.currentThread(), tracker);
            CompletionService<Void> completionService = new ExecutorCompletionService<>(threadPool);
            for (int i = 0; i < pageSize; i++) {
                final int start = i * fetchSize;
                completionService.submit(() -> {
                    Page<ItemPojo> itemPojoPage;
                    try {
                        Timer timer1 = new Timer();
                        timer1.start();
                        itemPojoPage = pcmRequester.listItems(start, fetchSize);
                        Duration stop1 = timer1.stop();
                        logger.debug("list item page[start {}, fetch {}] ok, cost {}", start, fetchSize,
                                stop1.toString());
                    } catch (RequestException e) {
                        logger.warn("list item fail, start {}, fetch {}", start, fetchSize);
                        return;
                    }
                    if (itemPojoPage.getContent() == null) {
                        return;
                    }
                    skuSet.addAll(
                            itemPojoPage.getContent().stream().map(ItemPojo::getSkuId).collect(Collectors.toList()));
                }, null);
            }
            //noinspection Duplicates
            try {
                for (int i = 0; i < pageSize; i++) {
                    completionService.take();
                }
            } catch (InterruptedException e) {
                Throwable throwable = tracker.get();
                throw new RuntimeException(e.toString(), throwable);
            }
            Duration stop = timer.stop();
            logger.debug("sku to warm-up counts: {}, cost {}", skuSet.size(), stop.toString());
            Set<String> spuSet = Collections.synchronizedSet(Sets.newHashSetWithExpectedSize(skuSet.size()));
            threadPool = ExecutorServiceFactory.create("warm-up-sku", threads, Thread.currentThread(), tracker);
            completionService = new ExecutorCompletionService<>(threadPool);
            AtomicInteger skuSum = new AtomicInteger();
            for (String skuId : skuSet) {
                completionService.submit(() -> {
                    SkuPojo skuPojo = null;
                    try {
                        Timer timer1 = new Timer();
                        timer1.start();
                        skuPojo = pcmRequester.directGetSkuInfo(skuId);
                        int i = skuSum.incrementAndGet();
                        Duration stop1 = timer1.stop();
                        logger.debug("warm-up sku[{}] ok, index {}, cost {}", skuId, i, stop1.toString());
                    } catch (RequestException e) {
                        logger.warn("get sku info fail, skuId {}", skuId);
                        return;
                    }
                    spuSet.add(skuPojo.getSpuId());
                }, null);
            }
            //noinspection Duplicates
            try {
                for (int i = 0; i < skuSet.size(); i++) {
                    completionService.take();
                }
            } catch (InterruptedException e) {
                Throwable throwable = tracker.get();
                throw new RuntimeException(e.toString(), throwable);
            }
            stop = timer.stop();
            logger.debug("warm-up {} skus, cost {}", skuSum.get(), stop.toString());
            threadPool = ExecutorServiceFactory.create("warm-up-spu", threads, Thread.currentThread(), tracker);
            completionService = new ExecutorCompletionService<>(threadPool);
            AtomicInteger spuSum = new AtomicInteger();
            for (String spuId : spuSet) {
                completionService.submit(() -> {
                    try {
                        Timer timer1 = new Timer();
                        timer1.start();
                        pcmRequester.directGetSpuInfo(spuId);
                        int i = spuSum.incrementAndGet();
                        Duration stop1 = timer1.stop();
                        logger.debug("warm-up spu[{}] ok, index {}, cost {}", spuId, i, stop1.toString());
                    } catch (RequestException e) {
                        logger.warn("get sku info fail, skuId {}", spuId);
                    }
                }, null);
            }
            //noinspection Duplicates
            try {
                for (int i = 0; i < spuSet.size(); i++) {
                    completionService.take();
                }
            } catch (InterruptedException e) {
                Throwable throwable = tracker.get();
                throw new RuntimeException(e.toString(), throwable);
            }
            stop = timer.stop();
            logger.debug("warm-up {} spus, cost {}", skuSum.get(), stop.toString());
            TimerStop lastStop = timer.lastStop();
            assert lastStop != null;
            Duration duration = new Duration(timer.getStartTime().toDateTime(), lastStop.getStopTime().toDateTime());
            logger.info("warm-up sku&spu cost {}", duration.toString());
            return null;
        } finally {
            running = false;
        }

    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
