package com.wfj.search.online.management.console.controller.interval;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import com.wfj.search.online.management.console.service.interval.IIntervalContentService;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
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
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/interval/content")
public class IntervalContentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IIntervalContentService intervalContentService;

    @RequestMapping("read")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-content-read")
    public JSONObject intervalContentList(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
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
            limit = params.getInteger("limit");
        if (limit == null) {
            limit = 20;
        }
        IntervalContentPojo intervalContent = null;
        try {
            intervalContent = JSONObject.toJavaObject(params.getJSONObject("intervalContent"),
                    IntervalContentPojo.class);
        } catch (Exception ignore) {
            // 未传入或传入的字段不对，直接忽略。
        }
        JSONObject json = new JSONObject();
        try {
            if (start < 0 || limit < 0) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重新请求！");
                response.setStatus(500);
            } else {
                List<IntervalContentPojo> list = intervalContentService.intervalContentListWithPage(intervalContent,
                        start, limit);
                json.put("success", true);// 成功标识
                json.put("total", intervalContentService.intervalContentTotal(intervalContent));// 总数
                json.put("count", list.size());
                json.put("list", list);// 结果列表
            }
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "查询应用列表出错！");
            logger.error("查询应用列表出错", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("create")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-content-create")
    public JSONObject createIntervalContent(@RequestBody(required = false) String message,
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
            IntervalContentPojo content = JSONObject.toJavaObject(params, IntervalContentPojo.class);
            int count = intervalContentService.addIntervalContent(content);
            if (count >= 1) {
                json.put("success", true);
            } else if (count == 0) {
                json.put("success", false);
                json.put("message", "请求新增的数据已经存在于数据库！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重新请求！");
                response.setStatus(500);
            }
            logger.debug("新增数据{}条，主键为{}，返回的json数据为：{}。", count, content.getSid(), json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "创建区间内容出错！");
            logger.error("创建区间内容出错 IntervalContentController.createIntervalContent", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("update")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-content-update")
    public JSONObject updateIntervalContent(@RequestBody(required = false) String message,
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
            IntervalContentPojo content = JSONObject.toJavaObject(params, IntervalContentPojo.class);
            int row = intervalContentService.updateIntervalContent(content);
            if (row >= 1) {
                json.put("success", true);
            } else if (row == 0) {
                json.put("success", false);
                json.put("message", "请求修改的数据未存在于数据库！");
                response.setStatus(500);
            } else if (row == -2) {
                json.put("success", false);
                json.put("message", "有效价格区间不能修改，请检查！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重新请求！");
                response.setStatus(500);
            }
            logger.debug("修改数据{}条，返回的json数据为：{}。", row, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "修改区间内容出错！");
            logger.error("修改区间内容出错 IntervalContentController.updateIntervalContent", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("destroy")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-content-destroy")
    public JSONObject deleteIntervalContent(@RequestBody(required = false) String message,
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
            IntervalContentPojo content = JSONObject.toJavaObject(params, IntervalContentPojo.class);
            int row = intervalContentService.deleteIntervalContent(content.getSid());
            if (row >= 1) {
                json.put("success", true);
            } else if (row == 0) {
                json.put("success", false);
                json.put("message", "请求删除的数据未存在于数据库！");
                response.setStatus(500);
            } else if (row == -2) {
                json.put("success", false);
                json.put("message", "有效价格区间不能删除，请检查！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重新请求！");
                response.setStatus(500);
            }
            logger.debug("删除数据{}条，返回的json数据为：{}。", row, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "删除区间内容出错！");
            logger.error("删除区间内容出错 IntervalContentController.deleteIntervalContent", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("doSelected")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-content-doSelected")
    public JSONObject doSelected(@RequestBody(required = false) String message,
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
            IntervalContentPojo content = JSONObject.toJavaObject(params, IntervalContentPojo.class);
            int row = intervalContentService.doSelectedInterval(content.getSid());
            if (row == 1) {
                json.put("success", true);
            } else if (row == -1) {
                json.put("success", false);
                json.put("message", "该价格区间内容不完整，请检查是否填写明细！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查！");
                response.setStatus(500);
            }
            logger.debug("选中价格区间{}条，返回的json数据为：{}。", row, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "选中价格区间出错，请检查！");
            logger.error("选中价格区间出错", e);
            response.setStatus(500);
        }
        return json;
    }
}
