package com.wfj.search.online.index.operation;

import com.wfj.search.online.common.pojo.BrandPojo;
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

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 品牌缓存预热操作
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
@Component("brandCacheWarmUpOperation")
public class BrandCacheWarmUpOperation implements IOperation<Void> {
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
            List<BrandPojo> brandPojos = pcmRequester.listBrands();
            logger.debug("brand to warm-up counts: {}", brandPojos.size());
            Timer timer = new Timer();
            timer.start();
            int threads = this.indexConfigService.getWarmUpFetchThreads();
            final AtomicReference<Throwable> tracker = new AtomicReference<>();
            ExecutorService threadPool = ExecutorServiceFactory.create("warm-up-brand", threads,
                    Thread.currentThread(), tracker);
            CompletionService<Void> completionService = new ExecutorCompletionService<>(threadPool);
            for (BrandPojo brandPojo : brandPojos) {
                completionService.submit(() -> {
                    Timer timer1 = new Timer();
                    timer1.start();
                    try {
                        pcmRequester.directGetBrandInfo(brandPojo.getBrandId());
                    } catch (RequestException e) {
                        logger.warn("warm-up brand[{}] failed", brandPojo.getBrandId());
                    }
                    Duration stop = timer1.stop();
                    logger.debug("brand[{}] warm-up, cost {}", brandPojo.getBrandId(), stop.toString());
                }, null);
            }
            //noinspection Duplicates
            try {
                for (int i = 0; i < brandPojos.size(); i++) {
                    completionService.take();
                }
            } catch (InterruptedException e) {
                Throwable throwable = tracker.get();
                throw new RuntimeException(e.toString(), throwable);
            }
            timer.stop();
            TimerStop lastStop = timer.lastStop();
            assert lastStop != null;
            Duration duration = new Duration(timer.getStartTime().toDateTime(), lastStop.getStopTime().toDateTime());
            logger.info("warm-up {} brands, cost {}", brandPojos.size(), duration.toString());
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
