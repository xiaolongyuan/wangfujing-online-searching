package com.wfj.search.online.web.controller;

import com.wfj.platform.util.concurrent.BatchRunnables;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SpuDisplayPojo;
import com.wfj.search.online.web.common.pojo.SuggestionDisplayPojo;
import com.wfj.search.online.web.service.ISearchConfigService;
import com.wfj.search.online.web.service.ISearchParamService;
import com.wfj.search.online.web.service.ISearchService;
import com.wfj.search.segment.SegmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.dowenliu.npl.dmseg.core.Token;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/search")
public class SearchController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ISearchParamService searchParamService;
    @Autowired
    private ISearchService searchService;
    @Autowired
    private SegmentService segmentService;
    @Autowired
    private ISearchConfigService searchConfigService;

    @RequestMapping("/{inputQuery}")
    public String search(Model model,
            @PathVariable("inputQuery") String inputQuery,
            @RequestParam(value = "fetch", required = false) Integer rows,
            @RequestParam(value = "channel", required = false) String channel,
            HttpServletRequest request) {
        SearchParams searchParams = null;
        try {
            searchParams = this.searchParamService
                    .restoreParams(inputQuery, rows, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", 1, channel);
        } catch (Exception ignored) {
            // 不会产生错误的，因为没有需要恢复的参数
        }
        assert searchParams != null;
        try {
            SearchResult searchResult = this.searchService.doSearch(searchParams, "/search");
            if (searchResult.getSuccessList().isEmpty()) {
                // 无结果
                List<Token> tokens = segmentService
                        .doSegment(inputQuery, SegmentService.Type.Query);
                if (!tokens.isEmpty()) {
                    final AtomicReference<Throwable> tracker = new AtomicReference<>();
                    final Thread masterThread = Thread.currentThread();
                    ExecutorService pool = Executors.newFixedThreadPool(5, r -> {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        thread.setName("simpleSearchThreads");
                        thread.setUncaughtExceptionHandler((t, e) -> {
                            tracker.set(e);
                            masterThread.interrupt();
                        });
                        return thread;
                    });
                    BatchRunnables batchRunnables = new BatchRunnables(pool);
                    tokens.forEach(token -> batchRunnables.addRunnable(() -> {
                        List<SpuDisplayPojo> spus = null;
                        try {
                            spus = this.searchService
                                    .simpleSearch(token.getValue(), this.searchConfigService.getSimpleRows(), channel)
                                    .getList();
                        } catch (TrackingException e) {
                            throw new RuntimeTrackingException(e);
                        }
                        if (!spus.isEmpty()) {
                            SuggestionDisplayPojo noResultSuggestion = new SuggestionDisplayPojo();
                            noResultSuggestion.setQuery(token.getValue());
                            noResultSuggestion.getList().addAll(spus);
                            searchResult.getNoResultSuggestion().add(noResultSuggestion);
                        }
                    }));
                    try {
                        batchRunnables.execute();
                    } catch (InterruptedException e) {
                        logger.warn("拆词建议搜索失败", e);
                        Throwable throwable = tracker.get();
                        if (throwable != null) {
                            if (throwable instanceof RuntimeTrackingException) {
                                throw ((RuntimeTrackingException) throwable).getTrackingException();
                            } else {
                                throw throwable;
                            }
                        }
                    }
                }
                model.addAttribute("result", searchResult);
                return "no_result";
            }
            model.addAttribute("result", searchResult);
            return "search";
        } catch (TrackingException e) {
            request.setAttribute("errorCode", e.getErrorCode());
            throw new RuntimeException(e);
        } catch (Throwable e) {
            logger.error("搜索异常, 0x530003", e);
            request.setAttribute("errorCode", "0x530003");
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/{inputQuery}/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0")
    public String search(Model model,
            @PathVariable("inputQuery") String inputQuery,
            @RequestParam(value = "fetch", required = false) Integer rows,
            @RequestParam(value = "channel", required = false) String channel,
            @PathVariable("cat1") String cat1,
            @PathVariable("cat2") String cat2,
            @PathVariable("cat3") String cat3,
            @PathVariable("brandIds") String brandIds,
            @PathVariable("price") String price,
            @PathVariable("standardIds") String standardIds,
            @PathVariable("colors") String colors,
            @PathVariable("attrs") String attrs,
            @PathVariable("tagIds") String tagIds,
            @PathVariable("sortId") String sortId,
            @PathVariable("currentPage") Integer currentPage,
            HttpServletRequest request) {
        SearchParams searchParams;
        try {
            searchParams = this.searchParamService
                    .restoreParams(inputQuery, rows, cat1, cat2, cat3, brandIds, price, standardIds, colors, attrs,
                            tagIds, sortId, currentPage, channel);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "无法恢复参数");
            model.addAttribute("exception", e);
            logger.error("无法恢复参数", e);
            throw new ResourceNotFoundException(); // 产生一个http 404 错误
        }
        try {
            SearchResult searchResult = this.searchService.doSearch(searchParams, "/search");
            model.addAttribute("success", true);
            model.addAttribute("result", searchResult);
            if (searchResult.getSuccessList().isEmpty()) {
                return "no_result";
            }
            return "search";
        } catch (TrackingException e) {
            request.setAttribute("errorCode", e.getErrorCode());
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("搜索异常, 0x530004", e);
            request.setAttribute("errorCode", "0x530004");
            throw e;
        }
    }
}
