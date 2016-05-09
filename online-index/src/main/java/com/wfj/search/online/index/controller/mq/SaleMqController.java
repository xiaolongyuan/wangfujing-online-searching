package com.wfj.search.online.index.controller.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.SoldPojo;
import com.wfj.search.online.index.service.IEsService;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.online.index.service.ISaleService;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.util.OperationHolderKt;
import com.wfj.search.util.web.record.MqWebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
import org.apache.commons.lang3.Validate;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>create at 15-12-9</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Controller
@RequestMapping("/mq/sale")
public class SaleMqController {
    private static final Logger logger = LoggerFactory.getLogger(SaleMqController.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
    @Autowired
    private ISaleService saleService;
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;

    @RequestMapping(value = "/sold", method = RequestMethod.POST)
    @ServiceRegister(name = "online-mq-statistics-sold")
    @MqWebOperation
    @JsonSignVerify
    public JSONObject sold(@RequestBody String message) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("asynchronous", false);
        JSONObject data = JSONObject.parseObject(message);
        // 取出下发消息
        JSONArray soldJsonArray = data.getJSONArray("messageBody");
        List<SoldPojo> solds = Lists.newArrayList();
        for (int i = 0; i < soldJsonArray.size(); i++) {
            JSONObject soldJson = soldJsonArray.getJSONObject(i);
            try {
                SoldPojo sold = new SoldPojo();
                sold.setItemId(Validate.notBlank(soldJson.getString("itemId"), "商品编码为空").trim());
                sold.setSkuId(Validate.notBlank(soldJson.getString("skuId"), "SKU编码为空").trim());
                sold.setSpuId(Validate.notBlank(soldJson.getString("spuId"), "SPU编码为空").trim());
                sold.setSales(Validate.notNull(soldJson.getInteger("sales"), "销售数量为空"));
                sold.setSaleTime(DATE_FORMAT.parse(Validate.notBlank(soldJson.getString("saleTime"), "销售时间为空").trim()));
                solds.add(sold);
            } catch (Exception e) {
                String msg = "messageBody转换为销售信息失败，格式错误，数据为：[ " + message + " ]";
                logger.error(msg, e);
                result.put("success", false);
                result.put("message", "messageBody转换为销售信息失败，" + e.toString());
                response.setStatus(500);
                return result;
            }
        }
        StringBuilder failureMsg = new StringBuilder();
        AtomicBoolean success = new AtomicBoolean(true);
        try {
            solds.forEach(sold -> {
                this.saleService.save(sold)
                        .ifPresent(failure -> {
                            failureMsg.append(failure.toString()).append("\n");
                            success.set(false);
                        });
                this.esService.updateSalesFromMySql(sold)
                        .ifPresent(failure -> {
                            failureMsg.append(failure.toString()).append("\n");
                            success.set(false);
                        });
                this.indexService.indexItemsOfSpuFromEs(sold.getSpuId(), Long.parseLong(operation.getSid()))
                        .ifPresent(failure -> {
                            failureMsg.append(failure.toString()).append("\n");
                            success.set(false);
                        });
            });
            try {
                this.indexService.commit();
            } catch (IndexException e) {
                logger.warn("commit solr索引失败", e);
            }
            result.put("success", success.get());
            if (!success.get()) {
                result.put("message", "销量下发异常，" + failureMsg);
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
