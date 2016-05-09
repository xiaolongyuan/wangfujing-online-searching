package com.wfj.search.online.management.console.controller;

import com.wfj.search.online.management.console.service.ICacheService;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * <br/>create at 15-12-21
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/searching-cache")
public class SearchingCacheController {
    private static final Logger logger = LoggerFactory.getLogger(SearchingCacheController.class);
    @Autowired
    private ICacheService cacheService;

    @RequestMapping("/clear-all-searching-cache")
    @ServiceRegister(name = "online-mc-search-cache-clear-all")
    public JSONObject clearAllSearchingCache(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("request body is {}, request parameter is {}", message, messageGet);
        JSONObject json = new JSONObject();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        try {
            cacheService.clearAllSearchingCache();
            json.put("success", true);
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "清除搜索模块缓存失败！");
            logger.error("清除搜索模块缓存失败", e);
            response.setStatus(500);
        }
        return json;
    }
}
