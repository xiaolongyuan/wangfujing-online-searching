package com.wfj.search.online.management.console.controller.interval;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;
import com.wfj.search.online.management.console.service.interval.IIntervalDetailService;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/service/interval/detail")
public class IntervalDetailController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IIntervalDetailService intervalDetailService;

    @RequestMapping("/list/{contentSid}")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-detail-list")
    public JSONObject intervalDetailList(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet,
            @PathVariable("contentSid") String contentSid) {
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
        } catch (Exception ignore) {
            // 未传入或传入start不是数字，直接使用默认的0
        }
        try {
            limit = Validate.notNull(params.getInteger("limit"));
        } catch (Exception ignore) {
            // 未传入或传入的limit不是数字，直接使用默认的20
        }
        IntervalDetailPojo intervalDetail = null;
        try {
            intervalDetail = JSONObject.toJavaObject(params.getJSONObject("intervalDetail"),
                    IntervalDetailPojo.class);
        } catch (Exception ignore) {
        }
        if (intervalDetail == null) {
            intervalDetail = new IntervalDetailPojo();
        }
        intervalDetail.setContentSid(contentSid);
        JSONObject json = new JSONObject();
        try {
            if (contentSid == null || contentSid.isEmpty() || contentSid.trim().equals("null")
                    || start < 0 || limit < 0) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重新请求！");
                response.setStatus(500);
            } else {
                List<IntervalDetailPojo> list = intervalDetailService.intervalDetailListWithPage(intervalDetail, start,
                        limit);
                json.put("success", true);// 成功标识
                json.put("total", intervalDetailService.intervalDetailTotal(intervalDetail));// 总数
                json.put("count", list.size());
                json.put("list", list);// 结果列表
            }
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "查询邮箱地址列表出错！");
            logger.error("查询邮箱地址列表出错 MonitorAppEmailController.monitorAppEmailList", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("create")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-detail-create")
    public JSONObject addIntervalDetail(@RequestBody(required = false) String message,
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
            IntervalDetailPojo detail = JSONObject.toJavaObject(params, IntervalDetailPojo.class);
            int count = intervalDetailService.createIntervalDetail(detail);
            if (count >= 1) {
                json.put("success", true);
            } else if (count == 0) {
                json.put("success", false);
                json.put("message", "请求新增的数据已经存在于数据库！");
                response.setStatus(500);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重新请求！");
                response.setStatus(500);
            }
            logger.debug("新增数据{}条，主键为{}，返回的json数据为：{}。", count, detail.getSid(), json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "创建区间明细出错！");
            logger.error("创建区间明细出错 IntervalDetailController.addIntervalDetail", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("update")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-detail-update")
    public JSONObject updateIntervalDetail(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        JSONObject json = new JSONObject();
        try {
            if (message == null || message.isEmpty()) {
                message = messageGet;
            }
            JSONObject msgJson = JSONObject.parseObject(message);
            JSONObject params = msgJson.getJSONObject("messageBody");
            IntervalDetailPojo detail = JSONObject.toJavaObject(params, IntervalDetailPojo.class);
            int count = intervalDetailService.updateIntervalDetail(detail);
            if (count >= 1) {
                json.put("success", true);
            } else if (count == 0) {
                json.put("success", false);
                json.put("message", "请求修改的数据未存在于数据库！");
                response.setStatus(500);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重新请求！");
                response.setStatus(500);
            }
            logger.debug("修改数据{}条，返回的json数据为：{}。", count, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "修改区间明细出错！");
            logger.error("修改区间明细出错 IntervalDetailController.updateIntervalDetail", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("destroy")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister(name = "online-mc-interval-detail-destroy")
    public JSONObject deleteIntervalDetail(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
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
            IntervalDetailPojo detail = JSONObject.toJavaObject(params, IntervalDetailPojo.class);
            int count = intervalDetailService.deleteIntervalDetail(detail);
            if (count >= 1) {
                json.put("success", true);
            } else if (count == 0) {
                json.put("success", false);
                json.put("message", "请求删除的数据未存在于数据库！");
                response.setStatus(500);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重新请求！");
                response.setStatus(500);
            }
            logger.debug("删除数据{}条，返回的json数据为：{}。", count, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "删除区间明细出错！");
            logger.error("删除区间明细出错 IntervalDetailController.deleteIntervalDetail", e);
            response.setStatus(500);
        }
        return json;
    }
}
