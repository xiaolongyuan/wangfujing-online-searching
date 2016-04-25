package com.wfj.search.online.index.coordinating;

import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;

/**
 * 全量构建ES数据协调任务
 * <br/>create at 15-7-28
 *
 * @author liufl
 * @since 1.0.0
 */
public class FullyRebuildEsTaskDescription extends AbstractCoordinatingTaskDescription {

    public FullyRebuildEsTaskDescription(Operation param,
            IOperation<Void> fullyRebuildEsOperation) {
        super("fullyRebuildEs", param, fullyRebuildEsOperation);
    }

    @Override
    public String getParamPath() {
        return "<>";
    }
}
