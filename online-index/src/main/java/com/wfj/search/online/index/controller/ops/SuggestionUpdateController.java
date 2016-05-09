package com.wfj.search.online.index.controller.ops;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.index.coordinating.SuggestionUpdateTaskDescription;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>create at 16-1-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/ops/suggestion")
public class SuggestionUpdateController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("suggestionUpdateOperation")
    private IOperation<Void> suggestionUpdateOperation;
    @Autowired
    private CuratorFramework client;
    @Value("${coordinator.operation.namespace}")
    private String taskNamespace;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;

    @RequestMapping("/fresh")
    @ServiceRegister(name = "online-freshSuggestion")
    @WebOperation
    @JsonSignVerify
    public JSONObject freshSuggestion(@RequestBody(required = false) String message,
            @RequestParam(value = "message", required = false) String messageGet) {
        logger.debug("post message body:" + message);
        logger.debug("param message=" + messageGet);
        JSONObject result = new JSONObject();
        Operation operation = OperationHolderKt.getOperation();
        result.put("success", true);
        result.put("asynchronous", true);
        Thread thread = new Thread(() -> {
            SuggestionUpdateTaskDescription taskDescription = new SuggestionUpdateTaskDescription(operation,
                    this.suggestionUpdateOperation);
            CoordinatingTask task = new CoordinatingTask(taskDescription, client, taskNamespace, instanceName);
            try {
                task.execute();
            } catch (Exception e) {
                logger.error("活动变化更新执行失败", e);
            }
        }, "manualFreshSuggestion-thread");
        thread.start();
        result.put("running", true);
        result.put("instance", this.instanceName);
        return result;
    }
}
