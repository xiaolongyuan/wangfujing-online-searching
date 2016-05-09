package com.wfj.search.online.index.cron;

import com.wfj.search.online.index.coordinating.ActivitiesUpdateTaskDescription;
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
 * <p>create at 16-1-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class ActivitiesUpdateJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("activeUpdateOperation")
    private IOperation<Void> activeUpdateOperation;
    @Autowired
    private CuratorFramework client;
    @Value("${coordinator.operation.namespace}")
    private String taskNamespace;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;
    @Value("${monitor.register.appName}")
    private String appName;

    public void updateActivities() {
        Operation operation = new Operation();
        operation.setAppName(this.appName);
        operation.setInstanceName(this.instanceName);
        operation.setStartTime(new Timestamp(System.currentTimeMillis()));
        operation.setOperation("ACTIVITIES_UPDATE");
        operation.setParameter("");
        operation.setCaller("SYS_INDEX_SERVICE");
        ActivitiesUpdateTaskDescription taskDescription = new ActivitiesUpdateTaskDescription(operation,
                this.activeUpdateOperation);
        CoordinatingTask task = new CoordinatingTask(taskDescription, client, taskNamespace, instanceName);
        try {
            task.execute();
        } catch (Exception e) {
            logger.error("活动变化更新执行失败", e);
        }
    }
}
