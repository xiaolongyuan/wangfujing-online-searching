package com.wfj.search.online.web.controller;

import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.service.ISearchParamService;
import com.wfj.search.online.web.service.ISearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/brandlist")
public class BrandListController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ISearchParamService searchParamService;
    @Autowired
    private ISearchService searchService;

    @RequestMapping("/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0")
    public String search(Model model,
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
        //noinspection Duplicates
        try {
            searchParams = this.searchParamService
                    .restoreParams(null, rows, cat1, cat2, cat3, brandIds, price, standardIds, colors, attrs,
                            tagIds, sortId, currentPage, channel);
            if (searchParams.getSelectedBrands().getSelected().size() != 1) {
                throw new IllegalArgumentException("品牌列表页面品牌条件非法");
            }
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "无法恢复参数");
            model.addAttribute("exception", e);
            logger.error("无法恢复参数", e);
            throw new ResourceNotFoundException(); // 产生一个http 404 错误
        }
        searchParams.setBrand(true);
        String urlPrefix = null;
        Object urlPrefix_ = request.getAttribute("urlPrefix");
        if (urlPrefix_ != null) {
            urlPrefix = urlPrefix_.toString();
        }
        SearchResult searchResult;
        try {
            searchResult = this.searchService.doBrandList(searchParams,
                    urlPrefix == null ? "/brandlist" : urlPrefix);
        } catch (TrackingException e) {
            request.setAttribute("errorCode", e.getErrorCode());
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("搜索异常, 0x530002", e);
            request.setAttribute("errorCode", "0x530002");
            throw e;
        }
        model.addAttribute("success", true);
        model.addAttribute("result", searchResult);
//        if (searchResult.getSuccessList().isEmpty()) {
//            throw new ResourceNotFoundException();
//        }
        return "brand";
    }
}
