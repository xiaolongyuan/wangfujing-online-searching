package com.wfj.search.online.index.controller.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.common.pojo.BrandPojo;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.service.IEsService;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.online.index.util.PojoUtils;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.util.OperationHolderKt;
import com.wfj.search.util.web.record.MqWebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>create at 15-11-3</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Controller
@RequestMapping("/mq/brand")
public class BrandMqController {
    private static final Logger logger = LoggerFactory.getLogger(BrandMqController.class);
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;

    @RequestMapping(value = "/indexBrandsAndItems", method = RequestMethod.POST)
    @ServiceRegister(value = "online-mq-indexBrands")
    @MqWebOperation
    @JsonSignVerify
    public JSONObject indexItems(@RequestBody String message) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("asynchronous", false);
        JSONObject data = JSONObject.parseObject(message);
        // 取出下发消息
        JSONArray brandJsonArray = data.getJSONArray("messageBody");
        List<BrandPojo> brandPojos = Lists.newArrayList();
        for (int i = 0; i < brandJsonArray.size(); i++) {
            JSONObject brandJson = brandJsonArray.getJSONObject(i);
            try {
                brandPojos.add(PojoUtils.json2Brand(brandJson));
            } catch (Exception e) {
                String msg = "messageBody转换为品牌POJO信息失败，格式错误，数据为：[ " + message + " ]";
                logger.error(msg, e);
                result.put("success", false);
                result.put("message", "messageBody转换为品牌POJO信息失败, Exception:" + e.toString());
                response.setStatus(500);
                return result;
            }
        }
        // 索引之
        StringBuilder failureMsg = new StringBuilder();
        AtomicBoolean success = new AtomicBoolean(true);
        try {
            brandPojos.stream().map(PojoUtils::toIndexPojo)
                    .forEach(brand ->
                            this.esService.updateBrand(brand)
                                    .ifPresent(failure -> {
                                        failureMsg.append(failure.toString()).append("\n");
                                        success.set(false);
                                    }));
            brandPojos.stream().map(BrandPojo::getBrandId)
                    .forEach(brandId ->
                            this.indexService.indexItemsOfBrandFromEs(brandId, Long.parseLong(operation.getSid()))
                                    .ifPresent(failure -> {
                                        failureMsg.append(failure.toString()).append("\n");
                                        success.set(false);
                                    }));
            try {
                this.indexService.commit();
            } catch (IndexException e) {
                logger.warn("commit solr索引失败", e);
            }
            result.put("success", success.get());
            if (!success.get()) {
                result.put("message", "修改品牌展示信息错误," + failureMsg);
                response.setStatus(500);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.toString());
            response.setStatus(500);
        }
        return result;
    }
}
