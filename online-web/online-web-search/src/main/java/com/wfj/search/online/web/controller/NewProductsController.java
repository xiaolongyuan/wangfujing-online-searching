package com.wfj.search.online.web.controller;

import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.service.ISearchConfigService;
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
 * <br/>create at 16-1-15
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller
public class NewProductsController {
    private static final Logger logger = LoggerFactory.getLogger(NewProductsController.class);
    @Autowired
    private ISearchParamService searchParamService;
    @Autowired
    private ISearchService searchService;
    @Autowired
    private ISearchConfigService searchConfigService;

    @RequestMapping("/new-products")
    public String show(Model model,
            @RequestParam(value = "fetch", required = false) Integer rows,
            @RequestParam(value = "channel", required = false) String channel,
            HttpServletRequest request) {
        return show(model, rows, channel, null, null, null, null, null, null, null, null, null, "2_1", 1, request);
    }

    @RequestMapping("/new-products/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0")
    public String show(Model model,
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
            searchParams = this.searchParamService.restoreParams(null, rows, cat1, cat2, cat3, brandIds, price,
                    standardIds, colors, attrs, tagIds, sortId, currentPage, channel);
            searchParams.setDateFrom(this.searchConfigService.getNewProductsDateFrom());
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "无法恢复参数");
            model.addAttribute("exception", e);
            logger.error("无法恢复参数", e);
            throw new ResourceNotFoundException(); // 产生一个http 404 错误
        }
        try {
            SearchResult searchResult = this.searchService.doNewProductsList(searchParams);
            model.addAttribute("success", true);
            model.addAttribute("result", searchResult);
//            if (searchResult.getSuccessList().isEmpty()) {
//                throw new ResourceNotFoundException();
//            }
            return "new-products";
        } catch (TrackingException e) {
            request.setAttribute("errorCode", e.getErrorCode());
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("搜索异常, 0x530006", e);
            request.setAttribute("errorCode", "0x530006");
            throw e;
        }
    }
}
