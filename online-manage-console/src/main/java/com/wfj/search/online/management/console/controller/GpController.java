package com.wfj.search.online.management.console.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.common.pojo.Gp;
import com.wfj.search.online.common.pojo.Page;
import com.wfj.search.online.management.console.service.GpService;
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

/**
 * <p>create at 16-5-11</p>
 *
 * @author liufl
 * @since 1.0.36
 */
@Controller
@RequestMapping("/service/gp")
public class GpController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GpService gpService;

    @RequestMapping("/listConfirmed")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-gp-list-confirmed")
    public JSONObject listConfirmed(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        if (logger.isDebugEnabled()) {
            logger.debug("message post\n{}", message);
            logger.debug("message get\n{}", messageGet);
        }
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        Integer start = params.getInteger("start");
        if (start == null) {
            start = 0;
        }
        Integer fetch = params.getInteger("fetch");
        if (fetch == null) {
            fetch = 10;
        }
        JSONObject json = new JSONObject();
        try {
            Page<Gp> gps = this.gpService.listConfirmed(start, fetch);
            json.put("list", gps.getContent());
            json.put("total", gps.getTotalElements());
            json.put("success", true);
        } catch (Exception e) {
            logger.error("查询Confirmed Gp失败", e);
            json.put("success", false);
            json.put("msg", e.toString());
        }
        return json;
    }

    @RequestMapping("/create")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-gp-create")
    public JSONObject createGp(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        if (logger.isDebugEnabled()) {
            logger.debug("message post\n{}", message);
            logger.debug("message get\n{}", messageGet);
        }
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        JSONObject json = new JSONObject();
        String title = params.getString("title");
        if (StringUtils.isBlank(title)) {
            json.put("success", false);
            json.put("msg", "标题为空");
            return json;
        }
        JSONArray itemIds = params.getJSONArray("itemIds");
        if (itemIds == null || itemIds.isEmpty()) {
            json.put("success", false);
            json.put("msg", "商品编码列表为空");
            return json;
        }
        Gp gp = new Gp();
        int size = itemIds.size();
        for (int i = 0; i < size; i++) {
            String itemId = itemIds.getString(i);
            gp.getItemIds().add(itemId);
        }
        gp.setTitle(title);
        try {
            this.gpService.createGp(gp);
        } catch (Exception e) {
            logger.error("创建Gp失败", e);
            json.put("success", false);
            json.put("msg", e.toString());
            return json;
        }
        json.put("success", true);
        json.put("gp", gp);
        return json;
    }

    @RequestMapping("/confirm")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-gp-confirm")
    public JSONObject confirmGp(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        if (logger.isDebugEnabled()) {
            logger.debug("message post\n{}", message);
            logger.debug("message get\n{}", messageGet);
        }
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        JSONObject json = new JSONObject();
        String gp = params.getString("gp");
        if (StringUtils.isBlank(gp)) {
            json.put("success", false);
            json.put("msg", "Gp ID为空");
            return json;
        }
        try {
            this.gpService.confirm(gp);
        } catch (Exception e) {
            logger.error("Confirm Gp失败", e);
            json.put("success", false);
            json.put("msg", e.toString());
            return json;
        }
        json.put("success", true);
        return json;
    }

    @RequestMapping("/delete")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-gp-delete")
    public JSONObject deleteGp(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        if (logger.isDebugEnabled()) {
            logger.debug("message post\n{}", message);
            logger.debug("message get\n{}", messageGet);
        }
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        JSONObject json = new JSONObject();
        String gp = params.getString("gp");
        if (StringUtils.isBlank(gp)) {
            json.put("success", false);
            json.put("msg", "Gp ID为空");
            return json;
        }
        try {
            this.gpService.delete(gp);
        } catch (Exception e) {
            logger.error("删除 Gp失败", e);
            json.put("success", false);
            json.put("msg", e.toString());
            return json;
        }
        json.put("success", true);
        return json;
    }
}
