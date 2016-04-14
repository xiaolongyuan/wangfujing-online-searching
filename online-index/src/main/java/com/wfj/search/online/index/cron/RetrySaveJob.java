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
 * <br/>create at 15-12-7
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Component("retrySaveJob")
public class RetrySaveJob {
    private static final Logger logger = LoggerFactory.getLogger(RetrySaveJob.class);
    @Autowired
    @Qualifier("retrySaveItemOperation")
    private IOperation<Void> retrySaveItemOperation;
    @Autowired
    @Qualifier("retrySaveCommentOperation")
    private IOperation<Void> retrySaveCommentOperation;
    @Value("${monitor.register.appName}")
    private String appName;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;

    public void retrySaveItem() {
        Operation operation = new Operation();
        operation.setAppName(this.appName);
        operation.setInstanceName(this.instanceName);
        operation.setStartTime(new Timestamp(System.currentTimeMillis()));
        operation.setOperation("ONLINE_RETRY_SAVE_ITEM");
        operation.setParameter("");
        operation.setCaller("SYS_INDEX_SERVICE");
        try {
            retrySaveItemOperation.operate(operation);
        } catch (Exception e) {
            logger.error("重建专柜商品ES数据任务失败", e);
        }
    }

    public void retrySaveComment() {
        Operation operation = new Operation();
        operation.setAppName(this.appName);
        operation.setInstanceName(this.instanceName);
        operation.setStartTime(new Timestamp(System.currentTimeMillis()));
        operation.setOperation("ONLINE_RETRY_SAVE_COMMENT");
        operation.setParameter("");
        operation.setCaller("SYS_INDEX_SERVICE");
        try {
            retrySaveCommentOperation.operate(operation);
        } catch (Exception e) {
            logger.error("重建评论消息ES数据任务失败", e);
        }
    }
}
