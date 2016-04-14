package com.wfj.search.online.management.console.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.common.pojo.CategoryPojo;
import com.wfj.search.online.management.console.service.cms.ICmsRequester;
import com.wfj.search.online.management.console.service.pcm.IPcmRequester;
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
import java.util.List;

/**
 * <br/>create at 15-11-20
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/basic-data")
public class BasicDataController {
    private static final Logger logger = LoggerFactory.getLogger(BasicDataController.class);
    @Autowired
    private IPcmRequester pcmRequester;
    @Autowired
    private ICmsRequester cmsRequester;

    @RequestMapping("/brands")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-basic-data-brands")
    public JSONObject brandList(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        if (logger.isDebugEnabled()) {
            logger.debug("message post\n" + message);
            logger.debug("message get\n" + messageGet);
        }
        JSONObject json = new JSONObject();
        json.put("success", true);// 成功标识
        JSONArray jsonArray = pcmRequester.listBrands();
        json.put("total", jsonArray.size());// 总数
        json.put("list", jsonArray);// 结果列表
        return json;
    }

    @RequestMapping("/categories")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-basic-data-categories")
    public JSONObject categoryList(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("request body is {}, request parameter is {}", message, messageGet);
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        String channel = null;
        try {
            channel = params.containsKey("channel") ? params.getString("channel") : null;
        } catch (Exception e) {
            logger.error("根据渠道查询分类树解析分类参数失败，获取到的参数：{}", message, e);
        }
        JSONObject json = new JSONObject();
        json.put("success", true);// 成功标识
        List<CategoryPojo> list = pcmRequester.listCategoryByChannel(channel);
        json.put("total", list.size());// 总数
        json.put("list", list);// 结果列表
        return json;
    }

    /**
     * 获取渠道列表
     *
     * @param message    参数
     * @param messageGet 参数
     * @return 渠道列表
     */
    @RequestMapping("/channels")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-basic-data-channels")
    public JSONObject channels(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        if (logger.isDebugEnabled()) {
            logger.debug("request body is {}, request param is {}", message, messageGet);
        }
        JSONObject json = new JSONObject();
        json.put("success", true);// 成功标识
        JSONArray jsonArray = pcmRequester.listChannels();
        json.put("total", jsonArray.size());// 总数
        json.put("list", jsonArray);// 结果列表
        return json;
    }

    /**
     * 获取站点列表
     *
     * @param message    参数
     * @param messageGet 参数
     * @return 站点列表
     */
    @RequestMapping("/getSiteList")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-basic-data-cms-sites")
    public JSONObject getSiteList(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        if (logger.isDebugEnabled()) {
            logger.debug("request body is {}, request param is {}", message, messageGet);
        }
        JSONObject json = new JSONObject();
        json.put("success", true);// 成功标识
        JSONArray jsonArray = cmsRequester.getSiteList();
        json.put("total", jsonArray.size());// 总数
        json.put("list", jsonArray);// 结果列表
        return json;
    }

    /**
     * 根据站点id获取频道列表
     *
     * @param message    参数
     * @param messageGet 参数
     * @return 频道列表
     */
    @RequestMapping("/getChannelListBySid")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-basic-data-cms-channels")
    public JSONObject getChannelListBySid(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        if (logger.isDebugEnabled()) {
            logger.debug("request body is {}, request param is {}", message, messageGet);
        }
        JSONObject json = new JSONObject();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        try {
            if (message == null || message.isEmpty()) {
                message = messageGet;
            }
            JSONObject msgJson = JSONObject.parseObject(message);
            JSONObject params = msgJson.getJSONObject("messageBody");
            String siteId = params.getString("siteId");
            if (StringUtils.isBlank(siteId)) {
                json.put("success", false);
                json.put("message", "站点不能为空，请检查！");
                response.setStatus(500);
            } else {
                json.put("success", true);// 成功标识
                JSONArray jsonArray = cmsRequester.getChannelListBySid(siteId);
                json.put("total", jsonArray.size());// 总数
                json.put("list", jsonArray);// 结果列表
            }
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "根据站点id查询频道列表出错！");
            logger.error("根据站点id查询频道列表出错", e);
            response.setStatus(500);
        }
        return json;
    }
}
