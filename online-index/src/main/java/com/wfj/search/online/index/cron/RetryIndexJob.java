package com.wfj.search.online.index.cron;

import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * <br/>create at 15-11-18
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Component("retryIndexJob")
public class RetryIndexJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("retryIndexItemOperation")
    private IOperation<Void> retryIndexItemOperation;
    @Autowired
    @Qualifier("retryIndexCommentOperation")
    private IOperation<Void> retryIndexCommentOperation;
    @Value("${monitor.register.appName}")
    private String appName;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;

    public void retryIndexItem() {
        Operation operation = new Operation();
        operation.setAppName(this.appName);
        operation.setInstanceName(this.instanceName);
        operation.setStartTime(new Timestamp(System.currentTimeMillis()));
        operation.setOperation("ONLINE_RETRY_INDEX_ITEM");
        operation.setParameter("");
        operation.setCaller("SYS_INDEX_SERVICE");
        try {
            retryIndexItemOperation.operate(operation);
        } catch (Exception e) {
            logger.error("重建专柜商品索引任务失败", e);
        }
    }

    public void retryIndexComment() {
        Operation operation = new Operation();
        operation.setAppName(this.appName);
        operation.setInstanceName(this.instanceName);
        operation.setStartTime(new Timestamp(System.currentTimeMillis()));
        operation.setOperation("ONLINE_RETRY_INDEX_COMMENT");
        operation.setParameter("");
        operation.setCaller("SYS_INDEX_SERVICE");
        try {
            retryIndexCommentOperation.operate(operation);
        } catch (Exception e) {
            logger.error("重建评论信息索引任务失败", e);
        }
    }
}
