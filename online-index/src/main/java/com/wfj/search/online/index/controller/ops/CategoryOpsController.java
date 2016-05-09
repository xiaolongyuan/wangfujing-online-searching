package com.wfj.search.online.index.controller.ops;

import com.alibaba.fastjson.JSONObject;
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
 * <p>create at 15-11-3</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Controller
@RequestMapping("/ops/category")
public class CategoryOpsController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;

    @RequestMapping("/indexItems")
    @ServiceRegister(name = "online-ops-indexCategory")
    @WebOperation
    @JsonSignVerify
    public JSONObject indexItems(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        logger.debug("post message body:" + message);
        logger.debug("param message=" + messageGet);
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("asynchronous", false);
        String categoryId;
        String channel;
        //noinspection Duplicates
        try {
            JSONObject messageBody = MessageBodyChooser.getJsonObject(message, messageGet);
            categoryId = Validate.notBlank(messageBody.getString("categoryId"), "分类编码为空").trim();
            channel = Validate.notBlank(messageBody.getString("channel"), "渠道编码为空").trim();
        } catch (Exception e) {
            logger.error("请求消息格式非法", e);
            result.put("success", false);
            result.put("message", "请求消息格式非法,Exception:" + e.toString());
            response.setStatus(500);
            return result;
        }
        StringBuilder failureMsg = new StringBuilder();
        try {
            result.put("success", true);
            Optional<Failure> failureOptional = this.esService.updateCategoryWithoutItems(categoryId);
            failureOptional.ifPresent(failure -> {
                failureMsg.append(failure.toString()).append("\n");
                if (failure.getDataType() == DataType.unknown && failure.getFailureType() == FailureType.initial) {
                    result.put("success", false);
                    result.put("message", failure.getMessage());
                    response.setStatus(500);
                }
            });
            if (failureOptional.isPresent()) {// 向ES保存分类数据失败
                Optional<Failure> esFailureOptional = this.esService
                        .updateItemOfCategory(categoryId, channel, Long.parseLong(operation.getSid()));
                //noinspection Duplicates
                esFailureOptional.ifPresent(failure -> {
                    failureMsg.append(failure.toString()).append("\n");
                    result.put("success", false);
                    result.put("message", failure.toString());
                    response.setStatus(500);
                });
                if (!esFailureOptional.isPresent()) {// 修改分类下的商品数据成功
                    Optional<Failure> indexFailureOptional = this.indexService.indexItemsOfCategoryFromEs(
                            categoryId, channel, Long.parseLong(operation.getSid()));
                    //noinspection Duplicates
                    indexFailureOptional.ifPresent(failure -> {
                        failureMsg.append(failure.toString()).append("\n");
                        result.put("success", false);
                        result.put("message", failure.toString());
                        response.setStatus(500);
                    });
                    if (!indexFailureOptional.isPresent()) {
                        try {
                            this.indexService.commit();
                        } catch (IndexException e) {
                            logger.warn("commit solr索引失败", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.toString());
            response.setStatus(500);
        } finally {
            result.put("failures", failureMsg);
        }
        return result;
    }
}
