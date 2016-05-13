package com.wfj.search.online.web.controller;

import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.common.pojo.Gp;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.service.IGpService;
import com.wfj.search.online.web.service.ISearchParamService;
import com.wfj.search.online.web.service.ISearchService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>create at 16-5-12</p>
 *
 * @author liufl
 * @since 1.0.36
 */
@Controller
public class GpController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IGpService gpService;
    @Autowired
    private ISearchParamService searchParamService;
    @Autowired
    private ISearchService searchService;

    @RequestMapping("/gpDisplay")
    public String gpDisplay(Model model,
            @RequestParam(value = "fetch", required = false) Integer rows,
            @RequestParam("gp") String gp,
            HttpServletRequest request) {
        SearchParams searchParams = this.searchParamService
                .restoreParams(null, rows, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", 1, null);
        this.restoreGpParams(searchParams, gp);
        try {
            SearchResult searchResult = this.searchService.doGpSearch(searchParams);
            model.addAttribute("success", true);
            model.addAttribute("result", searchResult);
        } catch (TrackingException e) {
            request.setAttribute("errorCode", e.getErrorCode());
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("搜索异常, 0x530091", e);
            request.setAttribute("errorCode", "0x530091");
            throw e;
        }
        return "gpDisplay";
    }

    private void restoreGpParams(SearchParams searchParams, String gpId) throws ResourceNotFoundException {
        if (StringUtils.isBlank(gpId)) {
            logger.error("gp为空");
            throw new ResourceNotFoundException();
        }
        searchParams.setGp(gpId);
        Gp gp = this.gpService.get(gpId);
        if (gp == null) {
            logger.error("恢复Gp为null");
            throw new ResourceNotFoundException();
        }
        searchParams.setGpTitle(gp.getTitle());
        searchParams.getGpItemIds().clear();
        searchParams.getGpItemIds().addAll(gp.getItemIds());
    }
}
