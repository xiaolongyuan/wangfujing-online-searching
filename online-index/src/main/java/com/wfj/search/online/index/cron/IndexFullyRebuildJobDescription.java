package com.wfj.search.online.index.cron;

import com.wfj.search.online.index.coordinating.FullyRebuildIndexTaskDescription;
import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.utils.zookeeper.coordinating.CoordinatingTask;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

/**
 * <br/>create at 15-7-22
 *
 * @author liufl
 * @since 1.0.0
 */
public class IndexFullyRebuildJobDescription {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("fullyBuildIndexFromEsOperation")
    private IOperation<Void> fullyBuildIndexOperation;
    @Autowired
    private CuratorFramework client;
    @Value("${coordinator.operation.namespace}")
    private String taskNamespace;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;
    @Value("${monitor.register.appName}")
    private String appName;

    public void fullyRebuildIndex() {
        Operation operation = new Operation();
        operation.setAppName(this.appName);
        operation.setInstanceName(this.instanceName);
        operation.setStartTime(new Timestamp(System.currentTimeMillis()));
        operation.setOperation("INDEX_REBUILD_FULLY");
        operation.setParameter("");
        operation.setCaller("SYS_INDEX_SERVICE");
        FullyRebuildIndexTaskDescription taskDescription = new FullyRebuildIndexTaskDescription(operation,
                this.fullyBuildIndexOperation);
        CoordinatingTask task = new CoordinatingTask(taskDescription, client, taskNamespace, instanceName);
        try {
            task.execute();
        } catch (Exception e) {
            logger.error("全量索引执行失败", e);
        }
    }
}
