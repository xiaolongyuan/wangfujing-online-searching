package com.wfj.search.online.index.coordinating;

import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;

import java.text.SimpleDateFormat;

/**
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
public class CategoryCacheWarmUpTaskDescription extends AbstractCoordinatingTaskDescription {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHH");

    public CategoryCacheWarmUpTaskDescription(Operation param,
            IOperation<Void> categoryCacheWarmUpOperation) {
        super("categoryCacheWarmUp", param, categoryCacheWarmUpOperation);
    }

    @Override
    public String getParamPath() {
        return DATE_FORMAT.format(this.getParam().getStartTime()) + "";
    }
}
