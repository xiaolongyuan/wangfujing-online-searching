package com.wfj.search.online.index.coordinating;

import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;

import java.text.SimpleDateFormat;

/**
 * <p>create at 16-1-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class ItemPriceChangesFetchTaskDescription extends AbstractCoordinatingTaskDescription {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");

    public ItemPriceChangesFetchTaskDescription(Operation param, IOperation<Void> itemPriceChangesFetchOperation) {
        super("itemPriceChangesFetch", param, itemPriceChangesFetchOperation);
    }

    @Override
    public String getParamPath() {
        return DATE_FORMAT.format(this.getParam().getStartTime()) + "";
    }
}
