package com.wfj.search.online.index.operation;

import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.service.IEsService;
import com.wfj.search.util.record.pojo.Operation;
import com.wfj.search.util.record.pojo.Status;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ES数据全量重建操作
 * <p>create at 15-7-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("fullyBuildEsOperation")
public class FullyBuildEsOperation implements IOperation<Void> {
    @Autowired
    private IEsService esService;
    private boolean running = false;

    public synchronized Void operate(Operation opRecord)
            throws RequestException, InterruptedException {
        try {
            running = true;
            this.esService.buildAll2Es(Long.parseLong(opRecord.getSid())).ifPresent(value -> {
                if (!value.isSuccess()) {
                    opRecord.setStatus(Status.FAILURE.getValue());
                }
                String comment = opRecord.getComment();
                String msg = value.toString();
                if (StringUtils.isBlank(comment)) {
                    opRecord.setComment(msg);
                } else {
                    if (comment.endsWith("\n")) {
                        opRecord.setComment(comment + msg);
                    } else {
                        opRecord.setComment(comment + '\n' + msg);
                    }
                }
            });
            running = false;
            return null;
        } finally {
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
