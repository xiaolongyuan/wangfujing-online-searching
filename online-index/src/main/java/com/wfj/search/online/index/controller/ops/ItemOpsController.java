package com.wfj.search.online.index.controller.ops;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.service.IEsService;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.online.index.util.MessageBodyChooser;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.util.OperationHolderKt;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
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
import java.util.Optional;

/**
 * <br/>create at 15-9-9
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/ops/item")
public class ItemOpsController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;

    @RequestMapping("/indexItem")
    @ServiceRegister(name = "online-ops-indexItem")
    @WebOperation
    @JsonSignVerify
    public JSONObject indexItem(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        logger.debug("post message body:" + message);
        logger.debug("param message=" + messageGet);
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("asynchronous", false);
        String itemId;
        //noinspection Duplicates
        try {
            JSONObject messageBody = MessageBodyChooser.getJsonObject(message, messageGet);
            itemId = Validate.notBlank(messageBody.getString("itemId"), "商品编码为空").trim();
        } catch (Exception e) {
            logger.error("请求消息格式非法", e);
            result.put("success", false);
            result.put("message", "请求消息格式非法,Exception:" + e.toString());
            response.setStatus(500);
            return result;
        }
        StringBuilder failureMsg = new StringBuilder();
        try {
            Optional<Failure> failureOptional = this.esService.buildItem(itemId, Long.parseLong(operation.getSid()));
            failureOptional.ifPresent(failure -> {
                if (DataType.item.equals(failure.getDataType()) && FailureType.initial
                        .equals(failure.getFailureType())) {
                    failureMsg.append("商品[").append(itemId).append("]似乎不存在\n");
                    response.setStatus(404);
                } else {
                    failureMsg.append(failure.toString()).append("\n");
                }
            });
            if (!failureOptional.isPresent()) {
                Optional<Failure> indexFailureOptional = this.indexService
                        .indexItemsFromEs(Lists.newArrayList(itemId), Long.parseLong(operation.getSid()));
                indexFailureOptional.ifPresent(failure -> failureMsg.append(failure.toString()).append("\n"));
                if (!indexFailureOptional.isPresent()) {
                    try {
                        this.indexService.commit();
                    } catch (IndexException e) {
                        logger.error("commit solr索引失败", e);
                    }
                    result.put("success", true);
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error("创建专柜商品[itemId" + itemId + "]索引失败", e);
            failureMsg.append(e.toString());
        }
        result.put("success", false);
        result.put("message", "专柜商品[" + itemId + "]创建索引失败");
        result.put("failures", failureMsg);
        response.setStatus(500);
        return result;
    }

    @RequestMapping("/removeItem")
    @ServiceRegister(name = "online-ops-removeItem")
    @WebOperation
    @JsonSignVerify
    public JSONObject removeItem(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        logger.debug("post message body:" + message);
        logger.debug("param message=" + messageGet);
        JSONObject result = new JSONObject();
        result.put("asynchronous", false);
        String itemId;
        //noinspection Duplicates
        try {
            JSONObject messageBody = MessageBodyChooser.getJsonObject(message, messageGet);
            itemId = Validate.notBlank(messageBody.getString("itemId"), "商品编码为空").trim();
        } catch (Exception e) {
            logger.error("请求消息格式非法", e);
            result.put("success", false);
            result.put("message", "请求消息格式非法,Exception:" + e.toString());
            response.setStatus(500);
            return result;
        }
        try {
            this.esService.removeItem(itemId);
            this.indexService.removeItem(itemId);
            try {
                this.indexService.commit();
            } catch (IndexException e) {
                logger.warn("commit solr索引失败", e);
            }
            result.put("success", true);
        } catch (Exception e) {
            String msg = "删除商品[itemId:" + itemId + "]失败";
            logger.error(msg, e);
            result.put("success", false);
            result.put("message", msg + ",Exception:" + e.toString());
            response.setStatus(500);
        }
        return result;
    }
}
