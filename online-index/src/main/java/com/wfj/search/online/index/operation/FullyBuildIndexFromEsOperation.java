package com.wfj.search.online.index.operation;

import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.util.record.pojo.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 索引全量重建操作
 * <p>create at 15-7-22</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Component("fullyBuildIndexFromEsOperation")
public class FullyBuildIndexFromEsOperation implements IOperation<Void> {
    @Autowired
    private IIndexService indexService;
    private boolean running = false;

    public synchronized Void operate(Operation opRecord)
            throws IndexException, RequestException, InterruptedException {
        try {
            running = true;
            this.indexService.indexAllFromEs(Long.parseLong(opRecord.getSid())).ifPresent(failure -> {
                String msg = failure.toString();
                String comment = opRecord.getComment();
                if (StringUtils.isBlank(comment)) {
                    opRecord.setComment(msg);
                } else {
                    if (comment.endsWith("\n")) {
                        opRecord.setComment(comment + msg);
                    } else {
                        opRecord.setComment(comment + "\n" + msg);
                    }
                }
            });
            this.indexService.commit();
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
