package com.wfj.search.online.management.console.controller;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.common.pojo.HotWordPojo;
import com.wfj.search.online.management.console.service.HotWordService;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
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
 * <br/>create at 15-12-15
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/hot-word")
public class HotWordController {
    private static final Logger logger = LoggerFactory.getLogger(HotWordController.class);
    @Autowired
    private HotWordService hotWordService;

    @RequestMapping("read")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-hot-word-read")
    public JSONObject read(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}, RequestParam is {}", message, messageGet);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        if (message == null || message.isEmpty()) {
            message = messageGet;
        }
        JSONObject msgJson = JSONObject.parseObject(message);
        JSONObject params = msgJson.getJSONObject("messageBody");
        int start = 0;
        int limit = 20;
        try {
            start = Validate.notNull(params.getInteger("start"));
            if (start < 0) {
                start = 0;
            }
        } catch (Exception ignore) {
            // 未传入或传入start不是数字，直接使用默认的0
        }
        try {
            limit = Validate.notNull(params.getInteger("limit"));
            if (limit < 0) {
                limit = 20;
            }
        } catch (Exception ignore) {
            // 未传入或传入的limit不是数字，直接使用默认的20
        }
        JSONObject json = new JSONObject();
        try {
            String site = params.containsKey("site") ? params.getString("site") : null;
            if (StringUtils.isBlank(site)) {
                site = null;
            }
            String channel = params.containsKey("channel") ? params.getString("channel") : null;
            if (StringUtils.isBlank(channel)) {
                channel = null;
            }
            List<HotWordPojo> list = hotWordService.list(site, channel, start, limit);
            json.put("success", true);// 成功标识
            json.put("total", hotWordService.count(site, channel));// 总数
            json.put("count", list.size());
            json.put("list", list);// 结果列表
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "查询热词失败！");
            logger.error("查询热词失败", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("get")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-hot-word-get")
    public JSONObject get(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}, RequestParam is {}", message, messageGet);
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
            String hotWordId = params.containsKey("hotWordId") ? params.getString("hotWordId") : null;
            HotWordPojo hotWord = hotWordService.get(hotWordId);
            if(hotWord == null) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重试！");
                response.setStatus(500);
            } else {
                json.put("success", true);// 成功标识
                json.put("data", hotWord);
            }
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "查询热词失败！");
            logger.error("查询热词失败", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("create")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-hot-word-create")
    public JSONObject create(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}, RequestParam is {}", message, messageGet);
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
            String site = params.containsKey("site") ? params.getString("site") : null;
            String channel = params.containsKey("channel") ? params.getString("channel") : null;
            String value = params.containsKey("value") ? params.getString("value") : null;
            String link = params.containsKey("link") ? params.getString("link") : null;
            int orders = params.containsKey("orders") ? params.getInteger("orders") : 0;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            HotWordPojo pojo = new HotWordPojo(site, channel, value, link, orders);
            int count = hotWordService.add(pojo, modifier);
            if (count == 1) {
                json.put("success", true);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重试！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "创建失败，请检查后重试！");
                response.setStatus(500);
            }
            logger.debug("新增{}条数据，返回{}。", count, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "新建热词失败！");
            logger.error("新建热词失败", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("destroy")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-hot-word-destory")
    public JSONObject destroy(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}, RequestParam is {}", message, messageGet);
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
            String sid = params.containsKey("sid") ? params.getString("sid") : null;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            int count = hotWordService.del(sid, modifier);
            if (count == 1) {
                json.put("success", true);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据");
                response.setStatus(500);
            } else if (count == -2) {
                json.put("success", false);
                json.put("message", "该数据未存在于数据库");
                response.setStatus(500);
            } else if (count == -3) {
                json.put("success", false);
                json.put("message", "有效数据不允许删除");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "删除热词失败");
                response.setStatus(500);
            }
            logger.debug("删除数据{}条，返回的json数据为：{}。", count, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "删除热词出错");
            logger.error("删除热词出错", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("update")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-hot-word-update")
    public JSONObject update(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}, RequestParam is {}", message, messageGet);
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
            String sid = params.containsKey("sid") ? params.getString("sid") : null;
            String site = params.containsKey("site") ? params.getString("site") : null;
            String channel = params.containsKey("channel") ? params.getString("channel") : null;
            String value = params.containsKey("value") ? params.getString("value") : null;
            String link = params.containsKey("link") ? params.getString("link") : null;
            int orders = params.containsKey("orders") ? params.getInteger("orders") : 0;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            HotWordPojo pojo = new HotWordPojo(sid, site, channel, value, link, orders);
            int count = hotWordService.mod(pojo, modifier);
            if (count == 1) {
                json.put("success", true);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据");
                response.setStatus(500);
            } else if (count == -2) {
                json.put("success", false);
                json.put("message", "该数据未存在于数据库");
                response.setStatus(500);
            } else if (count == -3) {
                json.put("success", false);
                json.put("message", "有效数据不允许修改");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "修改热词失败");
                response.setStatus(500);
            }
            logger.debug("修改数据{}条，返回的json数据为：{}。", count, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "修改热词失败");
            logger.error("修改热词失败", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("enabled")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-hot-word-enabled")
    public JSONObject enabled(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}, RequestParam is {}", message, messageGet);
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
            String sid = params.containsKey("sid") ? params.getString("sid") : null;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            int count = hotWordService.enabled(sid, modifier);
            if (count == 1) {
                json.put("success", true);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据");
                response.setStatus(500);
            } else if (count == -2) {
                json.put("success", false);
                json.put("message", "该数据未存在于数据库");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "使热词生效失败");
                response.setStatus(500);
            }
            logger.debug("使生效数据{}条，返回的json数据为：{}。", count, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "使热词生效失败");
            logger.error("使热词生效失败", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("disabled")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-hot-word-disabled")
    public JSONObject disabled(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("RequestBody is {}, RequestParam is {}", message, messageGet);
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
            String sid = params.containsKey("sid") ? params.getString("sid") : null;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            int count = hotWordService.disabled(sid, modifier);
            if (count == 1) {
                json.put("success", true);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据");
                response.setStatus(500);
            } else if (count == -2) {
                json.put("success", false);
                json.put("message", "该数据未存在于数据库");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "使热词失效失败");
                response.setStatus(500);
            }
            logger.debug("使失效数据{}条，返回的json数据为：{}。", count, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "使热词失效失败");
            logger.error("使热词失效失败", e);
            response.setStatus(500);
        }
        return json;
    }
}
