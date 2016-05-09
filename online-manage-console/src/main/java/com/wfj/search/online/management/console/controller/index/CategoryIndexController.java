package com.wfj.search.online.management.console.controller.index;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.management.console.service.index.ICategoryIndexService;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
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
 * <br/>create at 15-11-4
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/index/category")
public class CategoryIndexController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryIndexController.class);
    @Autowired
    private ICategoryIndexService categoryIndexService;

    @RequestMapping("/refresh-items-by-category")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-index-category-refresh-items")
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
            json = categoryIndexService.refreshItems(message);
            if (json.getBoolean("success")) {
                if (json.getBoolean("asynchronous")) {
                    json.put("asynchronous", false);
                }
            }
        } catch (Exception e) {
            json = new JSONObject();
            json.put("success", false);
            json.put("message", "根据分类刷新专柜商品服务异常，请稍后再试！");
            logger.error("根据分类刷新专柜商品服务异常 CategoryIndexController.refreshItems", e);
            response.setStatus(500);
        }
        return json;
    }
}
