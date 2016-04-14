package com.wfj.search.online.index.controller.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.common.pojo.CommentPojo;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.service.ICommentEsService;
import com.wfj.search.online.index.service.ICommentIndexService;
import com.wfj.search.online.index.util.PojoUtils;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.util.OperationHolderKt;
import com.wfj.search.util.web.record.MqWebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/mq/comment")
public class CommentMqController {
    private static final Logger logger = LoggerFactory.getLogger(CommentMqController.class);
    @Autowired
    private ICommentEsService commentEsService;
    @Autowired
    private ICommentIndexService indexService;

    @RequestMapping(value = "/indexComments", method = RequestMethod.POST)
    @ServiceRegister(value = "online-mq-indexComments")
    @MqWebOperation
    @JsonSignVerify
    public JSONObject indexComments(@RequestBody String message) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("asynchronous", false);
        JSONObject data = JSONObject.parseObject(message);
        // 取出下发消息
        JSONArray commentArray = data.getJSONArray("messageBody");
        List<CommentPojo> comments = new ArrayList<>();
        try {
            for (int i = 0; i < commentArray.size(); i++) {
                comments.add(PojoUtils.json2Comment(commentArray.getJSONObject(i)));
            }
        } catch (Exception e) {
            String msg = "messageBody转换为评论POJO信息失败，格式错误，数据为：[ " + message + " ]";
            logger.error(msg, e);
            result.put("success", false);
            result.put("message", "messageBody转换为评论POJO信息失败，" + e.toString());
            response.setStatus(500);
            return result;
        }
        StringBuilder failureMsg = new StringBuilder();
        AtomicBoolean success = new AtomicBoolean(true);
        try {
            comments.stream().map(comment -> PojoUtils.toIndexPojo(comment, Long.parseLong(operation.getSid())))
                    .forEach(comment -> commentEsService.updateComment(comment)
                            .ifPresent(value -> {
                                failureMsg.append(value.toString()).append("\n");
                                success.set(false);
                            }));
            comments.stream().map(CommentPojo::getSid)
                    .forEach(commentId -> indexService.saveFromEs(commentId, Long.parseLong(operation.getSid()))
                            .ifPresent(value -> {
                                failureMsg.append(value.toString()).append("\n");
                                success.set(false);
                            }));
            try {
                this.indexService.commit();
            } catch (IndexException e) {
                logger.warn("commit solr索引失败", e);
            }
            result.put("success", success.get());
            if (!success.get()) {
                result.put("message", "新增品论数据错误，" + failureMsg);
                response.setStatus(500);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.toString());
            response.setStatus(500);
        }
        return result;
    }

    /**
     * 评论暂时没有删除业务
     */
    @RequestMapping(value = "/removeComments", method = RequestMethod.POST)
    @ServiceRegister(value = "online-mq-removeComments")
    @MqWebOperation
    @JsonSignVerify
    public JSONObject removeComments(@RequestBody String message) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        JSONObject result = new JSONObject();
        result.put("asynchronous", false);
        JSONObject data = JSONObject.parseObject(message);
        // 取出下发消息
        JSONArray jsonArray = data.getJSONArray("messageBody");
        List<String> commentSidList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                commentSidList.add(jsonArray.getString(i));
            } catch (Exception e) {
                String msg = "messageBody转换为评论POJO信息失败，格式错误，数据为：[ " + message + " ]";
                logger.error(msg, e);
                result.put("success", false);
                result.put("message", "messageBody转换为评论POJO信息失败，" + e.toString());
                response.setStatus(500);
                return result;
            }
        }
        final List<String> failIds = Lists.newArrayList();
        commentSidList.forEach(commentId -> {
            if (StringUtils.isBlank(commentId)) {
                return;
            }
            try {
                this.commentEsService.removeComment(commentId);
                this.indexService.remove(commentId);
            } catch (IndexException e) {
                failIds.add(commentId);
                logger.error("删除评论索引失败, sid:[" + commentId + "]", e);
            }
        });
        if (failIds.isEmpty()) {
            result.put("success", true);
        } else {
            result.put("success", false);
            result.put("message", "以下评论索引删除失败：" + failIds.toString());
            response.setStatus(500);
        }
        return result;
    }
}
