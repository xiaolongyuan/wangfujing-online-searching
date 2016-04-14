package com.wfj.search.online.management.console.controller.operation;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.management.console.service.operation.IOperationPublicKeyService;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
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

/**
 * <br/>create at 15-12-9
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/operation/public-key")
public class OperationPublicKeyController {
    private static final Logger logger = LoggerFactory.getLogger(OperationPublicKeyController.class);
    @Autowired
    private IOperationPublicKeyService operationPublicKeyService;

    @WebOperation
    @JsonSignVerify
    @RequestMapping("/clear")
    @ServiceRegister("online-mc-operation-public-key-cache-clear")
    public JSONObject clear(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("request body is {}; request parameter is {}", message, messageGet);
        JSONObject json = new JSONObject();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes)
                .getResponse();
        try {
            operationPublicKeyService.clearPublicKey();
            json.put("success", true);
            logger.debug("清理公钥缓存成功");
        } catch (Exception e) {
            json.put("success", false);
            json.put("message", "清理公钥缓存异常，请稍后再试！");
            logger.error("清理公钥缓存异常", e);
            response.setStatus(500);
        }
        return json;
    }
}
