package com.wfj.search.online.index.controller.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.SpuPojo;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.service.IEsService;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.online.index.util.PojoUtils;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.util.OperationHolderKt;
import com.wfj.search.util.web.record.MqWebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
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
@RequestMapping("/mq/spu")
public class SpuMqController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;

    @RequestMapping(value = "/indexSpusAndItems", method = RequestMethod.POST)
    @ServiceRegister(name = "online-mq-indexSpus")
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
        JSONArray spuJsonArray = data.getJSONArray("messageBody");
        List<SpuPojo> spuPojos = Lists.newArrayList();
        for (int i = 0; i < spuJsonArray.size(); i++) {
            JSONObject spuJson = spuJsonArray.getJSONObject(i);
            try {
                spuPojos.add(PojoUtils.json2Spu(spuJson));
            } catch (Exception e) {
                String msg = "messageBody转换为SPU POJO信息失败，格式错误，数据为：[ " + message + " ]";
                logger.error(msg, e);
                result.put("success", false);
                result.put("message", "messageBody转换为SPU POJO信息失败，" + e.toString());
                response.setStatus(500);
                return result;
            }
        }
        // 索引之
        StringBuilder failureMsg = new StringBuilder();
        AtomicBoolean success = new AtomicBoolean(true);
        try {
            spuPojos.forEach(spu -> {
                this.esService.updateSpu(spu)
                        .ifPresent(failure -> {
                            failureMsg.append(failure.toString()).append("\n");
                            success.set(false);
                        });
                this.indexService.indexItemsOfSpuFromEs(spu.getSpuId(), Long.parseLong(operation.getSid()))
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
                result.put("message", "修改SPU展示信息错误，" + failureMsg);
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
