package com.wfj.search.online.management.console.controller.blacklist;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.common.pojo.blacklist.BlacklistPojo;
import com.wfj.search.online.management.console.service.blacklist.IBlackListService;
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
 * <br/>create at 15-10-28
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/blacklist")
public class BlacklistController {
    private static final Logger logger = LoggerFactory.getLogger(BlacklistController.class);
    @Autowired
    private IBlackListService blackListService;

    @RequestMapping("read")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-blacklist-read")
    public JSONObject getBlackList(@RequestBody(required = false) String message,
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
            String id = params.containsKey("id") ? params.getString("id") : null;
            if (StringUtils.isBlank(id)) {
                id = null;
            }
            String type = params.containsKey("type") ? params.getString("type") : null;
            if (StringUtils.isBlank(type)) {
                type = null;
            }
            List<BlacklistPojo> list = blackListService.getBlackList(id, type, start, limit);
            json.put("success", true);// 成功标识
            json.put("total", blackListService.getCount(id, type));// 总数
            json.put("count", list.size());
            json.put("list", list);// 结果列表
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "查询黑名单出错！");
            response.setStatus(500);
            logger.error("查询黑名单出错 BlacklistController.getBlackList", e);
        }
        return json;
    }

    @RequestMapping("create")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-blacklist-create")
    public JSONObject create(@RequestBody(required = false) String message,
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
            String id = params.containsKey("id") ? params.getString("id") : null;
            String type = params.containsKey("type") ? params.getString("type") : null;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            int flag = blackListService.add(id, type, modifier);
            if ((flag & BlacklistPojo.SUCCESS_INDEX) > 0 && (flag & BlacklistPojo.SUCCESS_DATABASE) > 0) {
                json.put("success", true);
            } else if ((flag & BlacklistPojo.SUCCESS_DATABASE) > 0) {
                json.put("success", false);
                json.put("message", "黑名单数据添加成功，删除索引失败，请手动删除！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重试！");
                response.setStatus(500);
            }
            logger.debug("新增{}条数据，返回{}。", flag, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "向黑名单添加数据出错！");
            logger.error("向黑名单添加数据出错 BlacklistController.create", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("delete")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-blacklist-delete")
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
            String id = params.containsKey("id") ? params.getString("id") : null;
            String type = params.containsKey("type") ? params.getString("type") : null;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            int flag = blackListService.del(id, type, modifier);
            if ((flag & BlacklistPojo.SUCCESS_INDEX) > 0 && (flag & BlacklistPojo.SUCCESS_DATABASE) > 0) {
                json.put("success", true);
            } else if ((flag & BlacklistPojo.SUCCESS_DATABASE) > 0) {
                json.put("success", false);
                json.put("message", "黑名单数据删除成功，刷新索引失败，请手动刷新！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重试！");
                response.setStatus(500);
            }
            logger.debug("删除{}条数据，返回{}。", flag, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "从黑名单中删除数据出错！");
            logger.error("从黑名单中删除数据出错 BlacklistController.delete", e);
            response.setStatus(500);
        }
        return json;
    }
}
