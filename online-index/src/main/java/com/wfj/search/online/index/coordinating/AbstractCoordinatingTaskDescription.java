package com.wfj.search.online.index.coordinating;

import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.utils.zookeeper.coordinating.CoordinatingTaskDescription;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
abstract class AbstractCoordinatingTaskDescription extends CoordinatingTaskDescription<Operation, JSONObject> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final IOperation<Void> operation;

    AbstractCoordinatingTaskDescription(String taskName, Operation param, IOperation<Void> operation) {
        super(taskName, param);
        this.operation = operation;
    }

    @Override
    public void executeTask() throws Exception {
        this.operation.operate(this.getParam());
        this.setSuccess(true);
        this.setResultDescription("更新活动变化完成");
        this.result = new JSONObject();
        String instanceName = this.getContext().getInstanceName();
        result.put("success", true);
        result.put("instance", instanceName);
        result.put("result", null);

    }

    @Override
    public void publishResult() throws Exception {
        if (this.getContext() == null) {
            throw new IllegalStateException("不是可执行的任务！");
        }
        String resultPath = this.getContext().getTaskPath() + "/publishResult";
        byte[] data = result.toString().getBytes("UTF-8");
        try {
            this.getContext().getClient().create().creatingParentsIfNeeded().forPath(resultPath, data);
        } catch (Exception e) {
            if (this.getContext().getClient().checkExists().forPath(resultPath) == null) {
                throw e;
            }
            this.getContext().getClient().setData().forPath(resultPath, data);
        }
    }

    @Override
    public boolean fetchPublishedResult() {
        if (this.getContext() == null) {
            throw new IllegalStateException("不是可执行的任务！");
        }
        String resultPath = this.getContext().getTaskPath() + "/publishResult";
        byte[] resultData = null;
        try {
            if (this.getContext().getClient().checkExists().forPath(resultPath) != null) {
                resultData = this.getContext().getClient().getData().forPath(resultPath);
            }
        } catch (Exception e) {
            logger.warn("从ZK获取结果信息异常", e);
        }
        if (resultData != null) {
            try {
                String result = new String(resultData, "UTF-8");
                logger.debug("{}取回{}{}结下结果:{}", getContext().getInstanceName(), taskName,
                        getContext().getCurrentTaskBatchSn(), result);
                this.result = JSONObject.fromObject(result);
                this.setSuccess(true);
                this.setResultDescription(this.getResult().toString());
            } catch (UnsupportedEncodingException ignored) {
            }
            return true;
        }
        return false;
    }
}
