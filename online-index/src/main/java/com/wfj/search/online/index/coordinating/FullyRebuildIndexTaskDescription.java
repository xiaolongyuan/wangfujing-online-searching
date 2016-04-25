package com.wfj.search.online.index.coordinating;

import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;

/**
 * 全量索引协调任务
 * <p>create at 15-12-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class FullyRebuildIndexTaskDescription extends AbstractCoordinatingTaskDescription {
    public FullyRebuildIndexTaskDescription(Operation param,
            IOperation<Void> fullyRebuildIndexOperation) {
        super("fullyRebuildIndex", param, fullyRebuildIndexOperation);
    }

    @Override
    public String getParamPath() {
        return "<>";
    }
}
