package com.wfj.search.online.index.cron;

import com.wfj.platform.util.zookeeper.coordinating.CoordinatingTask;
import com.wfj.search.online.index.coordinating.SkuSpuCacheWarmUpTaskDescription;
import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

/**
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
public class SkuSpuCacheWarmUpWarmJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("skuSpuCacheWarmUpOperation")
    private IOperation<Void> skuSpuCacheWarmUpOperation;
    @Autowired
    private CuratorFramework client;
    @Value("${coordinator.operation.namespace}")
    private String taskNamespace;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;
    @Value("${monitor.register.appName}")
    private String appName;

    public void warmUpSkuSpus() {
        Operation operation = new Operation();
        operation.setAppName(this.appName);
        operation.setInstanceName(this.instanceName);
        operation.setStartTime(new Timestamp(System.currentTimeMillis()));
        operation.setOperation("SKU_SPUS_WARM_UP");
        operation.setParameter("");
        operation.setCaller("SYS_INDEX_SERVICE");
        SkuSpuCacheWarmUpTaskDescription taskDescription = new SkuSpuCacheWarmUpTaskDescription(operation,
                this.skuSpuCacheWarmUpOperation);
        CoordinatingTask task = new CoordinatingTask(taskDescription, client, taskNamespace, instanceName);
        try {
            task.execute();
        } catch (Exception e) {
            logger.error("活动变化更新执行失败", e);
        }
    }
}
