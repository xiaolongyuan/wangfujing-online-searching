package com.wfj.search.online.index.cron;

import com.wfj.platform.util.zookeeper.coordinating.CoordinatingTask;
import com.wfj.search.online.index.coordinating.ItemPriceChangesFetchTaskDescription;
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
 * <p>create at 16-1-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class ItemPriceChangesFetchJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("itemPriceChangesFetchOperation")
    private IOperation<Void> itemPriceChangesFetchOperation;
    @Autowired
    private CuratorFramework client;
    @Value("${coordinator.operation.namespace}")
    private String taskNamespace;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;
    @Value("${monitor.register.appName}")
    private String appName;

    public void fetchItemPriceChanges() {
        Operation operation = new Operation();
        operation.setAppName(this.appName);
        operation.setInstanceName(this.instanceName);
        operation.setStartTime(new Timestamp(System.currentTimeMillis()));
        operation.setOperation("ITEM_PRICE_CHANGES");
        operation.setParameter("");
        operation.setCaller("SYS_INDEX_SERVICE");
        ItemPriceChangesFetchTaskDescription taskDescription = new ItemPriceChangesFetchTaskDescription(operation,
                this.itemPriceChangesFetchOperation);
        CoordinatingTask task = new CoordinatingTask(taskDescription, client, taskNamespace, instanceName);
        try {
            task.execute();
        } catch (Exception e) {
            logger.error("商品变价抓取执行失败", e);
        }
    }
}
