package com.wfj.search.online.index.controller.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.ItemPojo;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.service.IEsService;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.online.index.util.PojoUtils;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.util.OperationHolderKt;
import com.wfj.search.util.web.record.MqWebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
import org.apache.commons.lang3.StringUtils;
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
 * <p>create at 15-11-2</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Controller
@RequestMapping("/mq/item")
public class ItemMqController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;

    @RequestMapping(value = "/indexItems", method = RequestMethod.POST)
    @ServiceRegister(name = "online-mq-indexItems")
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
        JSONArray itemJsonArray = data.getJSONArray("messageBody");
        List<ItemPojo> itemPojos = Lists.newArrayList();
        for (int i = 0; i < itemJsonArray.size(); i++) {
            JSONObject itemJson = itemJsonArray.getJSONObject(i);
            try {
                itemPojos.add(PojoUtils.json2Item(itemJson));
            } catch (Exception e) {
                String msg = "messageBody转换为商品POJO信息失败，格式错误，数据为：[ " + message + " ]";
                logger.error(msg, e);
                result.put("success", false);
                result.put("message", "messageBody转换为商品POJO信息失败，" + e.toString());
                response.setStatus(500);
                return result;
            }
        }
        // 索引之
        StringBuilder failureMsg = new StringBuilder();
        AtomicBoolean success = new AtomicBoolean(true);
        try {
            itemPojos.forEach(itemPojo -> {
                this.esService.buildItem(itemPojo, Long.parseLong(operation.getSid()))
                        .ifPresent(failure -> {
                            failureMsg.append(failure.toString()).append("\n");
                            success.set(false);
                        });
                // FIXME 此处逻辑有问题：已经保存ES失败的item会在保存index时再次调用
                this.indexService.indexItemsFromEs(Lists.newArrayList(itemPojo.getItemId()), Long.parseLong(operation.getSid()))
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
                result.put("message", "专柜商品上架、变价、库存变化错误，" + failureMsg);
                response.setStatus(500);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.toString());
            response.setStatus(500);
        }
        return result;
    }

    @RequestMapping(value = "/removeItems", method = RequestMethod.POST)
    @ServiceRegister(name = "online-mq-removeItems")
    @MqWebOperation
    @JsonSignVerify
    public JSONObject removeItems(@RequestBody String message) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        JSONObject result = new JSONObject();
        result.put("asynchronous", false);
        JSONObject data = JSONObject.parseObject(message);
        // 取出下发消息
        JSONArray itemJsonArray = data.getJSONArray("messageBody");
        List<String> itemIds = Lists.newArrayList();
        for (int i = 0; i < itemJsonArray.size(); i++) {
            try {
                itemIds.add(itemJsonArray.getJSONObject(i).getString("itemId"));
            } catch (Exception e) {
                String msg = "messageBody转换为商品编码列表信息失败，格式错误，数据为：[ " + message + " ]";
                logger.error(msg, e);
                result.put("success", false);
                result.put("message", "messageBody转换为商品编码列表信息失败，" + e.toString());
                response.setStatus(500);
                return result;
            }
        }
        final List<String> failIds = Lists.newArrayList();
        for (String itemId : itemIds) {
            if (StringUtils.isBlank(itemId)) {
                continue;
            }
            try {
                this.esService.removeItem(itemId);
                this.indexService.removeItem(itemId);
                try {
                    this.indexService.commit();
                } catch (IndexException e) {
                    logger.warn("commit solr索引失败", e);
                }
            } catch (Exception e) {
                failIds.add(itemId);
                logger.error("删除Item索引失败, itemId:[" + itemId + "]", e);
            }
        }
        if (failIds.isEmpty()) {
            result.put("success", true);
        } else {
            result.put("success", false);
            result.put("message", "以下商品索引删除失败：" + failIds.toString());
            response.setStatus(500);
        }
        return result;
    }
}
