package com.wfj.search.online.index.controller.ops;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.coordinating.CoordinatingTask;
import com.wfj.platform.util.zookeeper.discovery.ServiceRegister;
import com.wfj.search.online.index.coordinating.FullyRebuildIndexTaskDescription;
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
public class FullyRebuildIndexController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("fullyBuildIndexFromEsOperation")
    private IOperation<Void> fullyBuildIndexFromEsOperation;
    @Autowired
    @Qualifier("fullyBuildIndexOperation")
    private IOperation<Void> fullyBuildIndexOperation;
    @Autowired
    private CuratorFramework client;
    @Value("${coordinator.operation.namespace}")
    private String taskNamespace;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;

    @RequestMapping("/ops/fullyRebuildIndex")
    @ServiceRegister(value = "online-fullyRebuildIndex")
    @WebOperation
    @JsonSignVerify
    public JSONObject fullyRebuildIndexManually(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("post message body:" + message);
        logger.debug("param message=" + messageGet);
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("success", true);
        result.put("asynchronous", true);
        Thread thread = new Thread(() -> {
            FullyRebuildIndexTaskDescription taskDescription = new FullyRebuildIndexTaskDescription(operation,
                    this.fullyBuildIndexOperation);
            CoordinatingTask task = new CoordinatingTask(taskDescription, client, taskNamespace,
                    instanceName);
            try {
                task.execute();
            } catch (Exception e) {
                logger.error("全量索引执行失败", e);
            }
        }, "manualFullyRebuildIndex-thread");
        thread.start();
        result.put("running", true);
        result.put("instance", this.instanceName);
        result.put("statusUrl", "/fullyRebuildIndexRunningStatus");
        return result;
    }

    @RequestMapping("/ops/fullyRebuildIndexFromEs")
    @ServiceRegister(value = "online-fullyRebuildIndexFromEs")
    @WebOperation
    @JsonSignVerify
    public JSONObject fullyRebuildIndexManuallyFromEs(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("post message body:" + message);
        logger.debug("param message=" + messageGet);
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("success", true);
        result.put("asynchronous", true);
        Thread thread = new Thread(() -> {
            FullyRebuildIndexTaskDescription taskDescription = new FullyRebuildIndexTaskDescription(operation,
                    this.fullyBuildIndexFromEsOperation);
            CoordinatingTask task = new CoordinatingTask(taskDescription, client, taskNamespace,
                    instanceName);
            try {
                task.execute();
            } catch (Exception e) {
                logger.error("全量索引执行失败", e);
            }
        }, "manualFullyRebuildIndex-thread");
        thread.start();
        result.put("running", true);
        result.put("instance", this.instanceName);
        result.put("statusUrl", "/fullyRebuildIndexRunningStatus");
        return result;
    }

    @RequestMapping("/fullyRebuildIndexRunningStatus")
    public String fullyRebuildIndexManually(Model model) {
        model.addAttribute("success", true);
        model.addAttribute("running", this.fullyBuildIndexFromEsOperation.isRunning());
        return "jsonView";
    }
}
