package com.wfj.search.online.management.console.controller.boost;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.common.pojo.boost.ManualBoostPojo;
import com.wfj.search.online.management.console.service.boost.IManualBoostService;
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
import java.util.List;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/manual-boost")
public class ManualBoostController {
    private static final Logger logger = LoggerFactory.getLogger(ManualBoostController.class);
    @Autowired
    private IManualBoostService manualBoostService;

    @RequestMapping("read")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-manual-boost-read")
    public JSONObject read(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}", message);
        logger.debug("RequestParam is {}", messageGet);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        Integer start;
        Integer limit;
            start = params.getInteger("start");
        if (start == null) {
            start = 0;
        }
            if (start < 0) {
                start = 0;
            }
            limit = params.getInteger("limit");
        if (limit == null) {
            limit = 20;
        }
            if (limit < 0) {
                limit = 20;
            }
        JSONObject json = new JSONObject();
        try {
            String skuId = params.containsKey("skuId") ? params.getString("skuId") : null;
            if (StringUtils.isBlank(skuId)) {
                skuId = null;
            }
            List<ManualBoostPojo> list = manualBoostService.list(skuId, start, limit);
            json.put("success", true);// 成功标识
            json.put("total", manualBoostService.count(skuId));// 总数
            json.put("count", list.size());
            json.put("list", list);// 结果列表
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "查询加权列表出错！");
            logger.error("查询加权列表出错!", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("save")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-manual-boost-save")
    public JSONObject save(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        JSONObject json = new JSONObject();
        try {
            String skuId = params.containsKey("skuId") ? params.getString("skuId") : null;
            String boost = params.containsKey("boost") ? params.getString("boost") : "0";
            int flag = manualBoostService.save(skuId, Float.valueOf(boost));
            if (flag == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重试！");
                response.setStatus(500);
            } else {
                json.put("success", true);
            }
            logger.debug("新增或修改{}条数据，返回{}。", flag, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "新增或修改加权数据出错！");
            logger.error("新增或修改加权数据出错", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("delete")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-manual-boost-delete")
    public JSONObject delete(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        JSONObject json = new JSONObject();
        try {
            String skuId = params.containsKey("skuId") ? params.getString("skuId") : null;
            int flag = manualBoostService.delete(skuId);
            if (flag > 0) {
                json.put("success", true);
            } else {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重试！");
                response.setStatus(500);
            }
            logger.debug("删除{}条数据，返回{}。", flag, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "从加权列表中删除数据出错！");
            logger.error("从加权列表中删除数据出错！", e);
            response.setStatus(500);
        }
        return json;
    }
}
