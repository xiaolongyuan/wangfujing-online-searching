package com.wfj.search.online.management.console.controller.popularize;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.common.pojo.popularize.BrandPopularizePositionPojo;
import com.wfj.search.online.management.console.service.popularize.IBrandPopularizePositionService;
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
 * <br/>create at 15-8-21
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/brand-popularize-position")
public class BrandPopularizePositionController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IBrandPopularizePositionService brandPopularizePositionService;

    @RequestMapping("read")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-top-spot-brand-read")
    public JSONObject branPopularizePositionList(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
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
        BrandPopularizePositionPojo position = null;
        try {
            position = JSONObject.toJavaObject(params.getJSONObject("position"),
                    BrandPopularizePositionPojo.class);
        } catch (Exception ignore) {
            // 未传入或传入的字段不对，直接忽略。
        }
        JSONObject json = new JSONObject();
        if (start < 0 || limit < 0) {
            json.put("success", false);
            json.put("message", "未输入正确的数据，请检查后重新请求！");
            response.setStatus(500);
            return json;
        }
        try {
            List<BrandPopularizePositionPojo> list = brandPopularizePositionService
                    .listWithPage(position, start, limit);
            json.put("success", true);// 成功标识
            json.put("total", brandPopularizePositionService.brandPositionTotal(position));// 总数
            json.put("count", list.size());// 当前返回条数
            json.put("list", list);// 结果列表
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "查询品牌坑位列表出错！");
            logger.error("查询品牌坑位列表出错 BrandPopularizePositionController.branPopularizePositionList", e);
            response.setStatus(500);

        }
        return json;
    }

    @RequestMapping("create")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-top-spot-brand-create")
    public JSONObject addBrandPosition(@RequestBody(required = false) String message,
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
            String brandId = params.containsKey("brandId") ? params.getString("brandId") : null;
            String spuId = params.containsKey("spuId") ? params.getString("spuId") : null;
            int orders = params.containsKey("orders") ? params.getInteger("orders") : 0;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            BrandPopularizePositionPojo position = new BrandPopularizePositionPojo(brandId, spuId, orders, modifier);
            int count = brandPopularizePositionService.addBrandPosition(position, modifier);
            if (count == 1) {
                json.put("success", true);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重试！");
                response.setStatus(500);
            } else if (count == -2) {
                json.put("success", false);
                json.put("message", "该数据已经存在于数据库，请勿重复添加！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "创建失败，请检查后重试！");
                response.setStatus(500);
            }
            logger.debug("新增数据{}条，主键为{}，返回的json数据为：{}。", count, position.getSid(), json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "创建失败，请检查后重试！");
            logger.error("创建品牌坑位出错 BrandPopularizePositionController.addBrandPosition", e);
            response.setStatus(500);
        }
        return json;
    }

    @RequestMapping("destroy")
    @WebOperation
    @JsonSignVerify
    @ServiceRegister("online-mc-top-spot-brand-destroy")
    public JSONObject deleteBrandPosition(@RequestBody(required = false) String message,
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
            String sid = params.containsKey("sid") ? params.getString("sid") : null;
            String brandId = params.containsKey("brandId") ? params.getString("brandId") : null;
            String spuId = params.containsKey("spuId") ? params.getString("spuId") : null;
            int orders = params.containsKey("orders") ? params.getInteger("orders") : 0;
            String modifier = null;
            if (msgJson.containsKey("username")) {
                modifier = msgJson.getString("username");
            }
            if (StringUtils.isBlank(modifier)) {
                modifier = "NOT GIVEN";
            }
            BrandPopularizePositionPojo position = new BrandPopularizePositionPojo(sid, brandId, spuId, orders);
            int count = brandPopularizePositionService.deleteBrandPosition(position, modifier);
            if (count == 1) {
                json.put("success", true);
            } else if (count == -1) {
                json.put("success", false);
                json.put("message", "未输入正确的数据，请检查后重试！");
                response.setStatus(500);
            } else if (count == -2) {
                json.put("success", false);
                json.put("message", "该数据未存在于数据库，请检查后重试！");
                response.setStatus(500);
            } else {
                json.put("success", false);
                json.put("message", "删除失败，请检查后重试！");
                response.setStatus(500);
            }
            logger.debug("删除数据{}条，返回的json数据为：{}。", count, json.toString());
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "删除失败，请检查后重试！");
            logger.error("删除品牌坑位出错 BrandPopularizePositionController.deleteBrandPosition", e);
            response.setStatus(500);
        }
        return json;
    }
}
