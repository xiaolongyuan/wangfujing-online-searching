package com.wfj.search.online.index.controller.ops;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.index.coordinating.FullyRebuildIndexTaskDescription;
import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.util.OperationHolderKt;
import com.wfj.search.util.web.record.WebOperation;
import com.wfj.search.utils.web.signature.verify.JsonSignVerify;
import com.wfj.search.utils.zookeeper.coordinating.CoordinatingTask;
import com.wfj.search.utils.zookeeper.discovery.ServiceRegister;
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
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/ops/comment")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    @Qualifier("fullyBuildCommentOperation")
    private IOperation<Void> fullyBuildCommentOperation;
    @Autowired
    private CuratorFramework client;
    @Value("${coordinator.operation.namespace}")
    private String taskNamespace;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;

    @RequestMapping("/fullyRebuildComment")
    @ServiceRegister(name = "online-fullyRebuildComment")
    @WebOperation
    @JsonSignVerify
    public JSONObject fullyRebuildComment(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("post message body:" + message);
        logger.debug("param message=" + messageGet);
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("success", true);
        result.put("asynchronous", true);
        Thread thread = new Thread(() -> {
            FullyRebuildIndexTaskDescription taskDescription = new FullyRebuildIndexTaskDescription(operation,
                    this.fullyBuildCommentOperation);
            CoordinatingTask task = new CoordinatingTask(taskDescription, client, taskNamespace, instanceName);
            try {
                task.execute();
            } catch (Exception e) {
                logger.error("全量索引执行失败", e);
            }
        }, "fullyRebuildComment-thread");
        thread.start();
        result.put("running", true);
        result.put("instance", this.instanceName);
        result.put("statusUrl", "/fullyRebuildCommentStatus");
        return result;
    }

    @RequestMapping("/fullyRebuildCommentStatus")
    public String fullyRebuildCommentStatus(Model model) {
        model.addAttribute("success", true);
        model.addAttribute("running", this.fullyBuildCommentOperation.isRunning());
        return "jsonView";
    }
}
