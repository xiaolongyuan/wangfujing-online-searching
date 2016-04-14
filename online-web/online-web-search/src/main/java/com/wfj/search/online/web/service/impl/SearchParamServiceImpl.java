package com.wfj.search.online.web.service.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.param.*;
import com.wfj.search.online.web.service.ISearchConfigService;
import com.wfj.search.online.web.service.ISearchParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("searchParamService")
public class SearchParamServiceImpl implements ISearchParamService {
    @Autowired
    private ISearchConfigService searchConfigService;
    @Autowired
    private QParamRestorer qParamRestorer;
    @Autowired
    private RowsParamRestorer rowsParamRestorer;
    @Autowired
    private CurrentPageParamRestorer currentPageParamRestorer;
    @Autowired
    private CatIdPathParamRestorer catIdPathParamRestorer;
    @Autowired
    private BrandIdsParamRestorer brandIdsParamRestorer;
    @Autowired
    private PriceRangeParamRestorer priceRangeParamRestorer;
    @Autowired
    private StandardIdsParamRestorer standardIdsParamRestorer;
    @Autowired
    private ColorsParamRestorer colorsParamRestorer;
    @Autowired
    private PropsParamRestorer propsParamRestorer;
    @Autowired
    private TagIdsParamRestorer tagIdsParamRestorer;
    @Autowired
    private SortParamRestorer sortParamRestorer;

    @Override
    public SearchParams restoreParams(String inputQuery, Integer rows, String cat1, String cat2, String cat3,
            String brandIds, String price, String standardIds, String colors, String attrs, String tagIds,
            String sortId, int currentPage, String channel) {
        final SearchParams searchParams = new SearchParams();
        searchParams.setChannel(StringUtils.isBlank(channel) ? searchConfigService.getChannel() : channel);
        List<Runnable> runnableList = Lists.newArrayList();
        runnableList.add(() -> qParamRestorer.restore(searchParams, inputQuery));
        runnableList.add(() -> rowsParamRestorer.restore(searchParams, rows));
        runnableList.add(() -> currentPageParamRestorer.restore(searchParams, currentPage));
        runnableList.add(() -> catIdPathParamRestorer.restore(searchParams, Lists.newArrayList(cat1, cat2, cat3)));
        runnableList.add(() -> brandIdsParamRestorer.restore(searchParams, brandIds));
        runnableList.add(() -> priceRangeParamRestorer.restore(searchParams, price));
        runnableList.add(() -> standardIdsParamRestorer.restore(searchParams, standardIds));
        runnableList.add(() -> colorsParamRestorer.restore(searchParams, colors));
        runnableList.add(() -> propsParamRestorer.restore(searchParams, attrs));
        runnableList.add(() -> tagIdsParamRestorer.restore(searchParams, tagIds));
        runnableList.add(() -> sortParamRestorer.restore(searchParams, sortId));
        final Thread mainThread = Thread.currentThread();
        final AtomicReference<Throwable> atomicException = new AtomicReference<>();
        ExecutorService pool = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r, "SearchParamRestoreThreads");
            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler((t, e) -> {
                atomicException.set(e);
                mainThread.interrupt();
            });
            return thread;
        });
        CompletionService<Void> completionService = new ExecutorCompletionService<>(pool);
        runnableList.forEach(r -> completionService.submit(r, null));
        try {
            for (int i = 0; i < runnableList.size(); i++) {
                completionService.take();
            }
        } catch (InterruptedException e) {
            Throwable throwable = atomicException.get();
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new RuntimeException(throwable.getMessage(), throwable);
            }
        } finally {
            pool.shutdown();
        }
        return searchParams;
    }
}
