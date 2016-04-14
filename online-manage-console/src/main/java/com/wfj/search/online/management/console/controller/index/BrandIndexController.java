package com.wfj.search.online.management.console.controller.index;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.management.console.service.index.IBrandIndexService;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import org.apache.commons.lang3.StringUtils;
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
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/index/brand")
public class BrandIndexController {
    private static final Logger logger = LoggerFactory.getLogger(BrandIndexController.class);
    @Autowired
    private IBrandIndexService brandIndexService;

    @RequestMapping("/refresh-items-by-brand")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-index-brand-refresh-items")
    public JSONObject refreshItems(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}", message);
        logger.debug("RequestParam is {}", messageGet);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        if (StringUtils.isBlank(message)) {
            message = messageGet;
        }
        JSONObject json;
        try {
            json = brandIndexService.refreshItems(message);
            if (json.getBoolean("success")) {
                if (json.getBoolean("asynchronous")) {
                    json.put("asynchronous", false);
                }
            }
        } catch (Exception e) {
            json = new JSONObject();
            json.put("success", false);
            json.put("message", "根据品牌刷新专柜商品服务异常，请稍后再试！");
            logger.error("根据品牌刷新专柜商品服务异常 BrandIndexController.refreshItems", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("/remove-items-by-brand")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-index-brand-remove-items")
    public JSONObject removeItems(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}", message);
        logger.debug("RequestParam is {}", messageGet);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        if (StringUtils.isBlank(message)) {
            message = messageGet;
        }
        JSONObject json;
        try {
            json = brandIndexService.removeItems(message);
            if (json.getBoolean("success")) {
                if (json.getBoolean("asynchronous")) {
                    json.put("asynchronous", false);
                }
            }
        } catch (Exception e) {
            json = new JSONObject();
            json.put("success", false);
            json.put("message", "根据品牌下架专柜商品服务异常，请稍后再试！");
            logger.error("根据品牌下架专柜商品服务异常 BrandIndexController.removeItems", e);
            response.setStatus(500);
        }
        return json;
    }
}
