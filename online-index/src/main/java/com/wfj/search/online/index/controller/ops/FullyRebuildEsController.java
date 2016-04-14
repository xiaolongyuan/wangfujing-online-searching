package com.wfj.search.online.index.controller.ops;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.coordinating.CoordinatingTask;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.index.coordinating.FullyRebuildEsTaskDescription;
import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.util.OperationHolderKt;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <br/>create at 15-7-23
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
public class FullyRebuildEsController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("fullyBuildEsOperation")
    private IOperation<Void> fullyBuildEsOperation;
    @Autowired
    private CuratorFramework client;
    @Value("${coordinator.operation.namespace}")
    private String taskNamespace;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;

    @RequestMapping("/ops/fullyRebuildEs")
    @ServiceRegister(value = "online-fullyRebuildES")
    @WebOperation
    @JsonSignVerify
    public JSONObject fullyRebuildEsManually(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("post message body:" + message);
        logger.debug("param message=" + messageGet);
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("success", true);
        result.put("asynchronous", true);
        Thread thread = new Thread(() -> {
            FullyRebuildEsTaskDescription taskDetail = new FullyRebuildEsTaskDescription(operation,
                    this.fullyBuildEsOperation);
            CoordinatingTask task = new CoordinatingTask(taskDetail, client, taskNamespace,
                    instanceName);
            try {
                task.execute();
            } catch (Exception e) {
                logger.error("全量构建ES执行失败", e);
            }
        }, "manualFullyRebuildEs-thread");
        thread.start();
        result.put("running", true);
        result.put("instance", this.instanceName);
        result.put("statusUrl", "/fullyRebuildEsRunningStatus");
        return result;
    }

    @RequestMapping("/fullyRebuildEsRunningStatus")
    public String fullyRebuildEsRunningStatus(Model model) {
        model.addAttribute("success", true);
        model.addAttribute("running", this.fullyBuildEsOperation.isRunning());
        return "jsonView";
    }
}
