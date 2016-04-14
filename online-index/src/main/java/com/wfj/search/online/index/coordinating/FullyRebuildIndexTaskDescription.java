package com.wfj.search.online.index.coordinating;

import com.wfj.platform.util.zookeeper.coordinating.CoordinatingTaskDescription;
import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 全量索引协调任务
 * <p>create at 15-12-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class FullyRebuildIndexTaskDescription extends CoordinatingTaskDescription<Operation, JSONObject> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final IOperation<Void> fullyRebuildIndexOperation;

    public FullyRebuildIndexTaskDescription(Operation param,
            IOperation<Void> fullyRebuildIndexOperation) {
        super("fullyRebuildIndex", param);
        this.fullyRebuildIndexOperation = fullyRebuildIndexOperation;
    }

    @Override
    public String getParamPath() {
        return "<>";
    }

    @Override
    public void executeTask() throws Exception {
        this.fullyRebuildIndexOperation.operate(this.getParam());
        this.setSuccess(true);
        this.setResultDescription("全量索引完成");
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
