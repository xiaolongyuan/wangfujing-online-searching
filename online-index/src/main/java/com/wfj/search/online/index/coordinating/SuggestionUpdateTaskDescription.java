package com.wfj.search.online.index.coordinating;

import com.wfj.search.online.index.operation.IOperation;
import com.wfj.search.util.record.pojo.Operation;

import java.text.SimpleDateFormat;

/**
 * <p>create at 16-1-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SuggestionUpdateTaskDescription extends AbstractCoordinatingTaskDescription {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHH");

    public SuggestionUpdateTaskDescription(Operation param, IOperation<Void> suggestionUpdateOperation) {
        super("suggestionUpdate", param, suggestionUpdateOperation);
    }

    @Override
    public String getParamPath() {
        return DATE_FORMAT.format(this.getParam().getStartTime()) + "";
    }
}
