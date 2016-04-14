package com.wfj.search.online.index.operation;

import com.wfj.search.online.index.service.ICommentEsService;
import com.wfj.search.online.index.service.ICommentIndexService;
import com.wfj.search.util.record.pojo.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Component("fullyBuildCommentOperation")
public class FullyBuildCommentOperation implements IOperation<Void> {
    @Autowired
    private ICommentEsService commentEsService;
    @Autowired
    private ICommentIndexService commentIndexService;
    private boolean running = false;

    @Override
    public Void operate(Operation operation) throws Exception {
        try {
            running = true;
            StringBuilder msg = new StringBuilder();
            commentEsService.buildComments(Long.parseLong(operation.getSid()))
                    .ifPresent(value -> msg.append(value.toString()));
            commentIndexService.indexAllFromEs(Long.parseLong(operation.getSid()))
                    .ifPresent(value -> msg.append(value.toString()));
            String comment = operation.getComment();
            if (StringUtils.isBlank(comment)) {
                operation.setComment(msg.toString());
            } else {
                if (comment.endsWith("\n")) {
                    operation.setComment(comment + msg);
                } else {
                    operation.setComment(comment + '\n' + msg);
                }
            }
            commentIndexService.commit();
        } finally {
            running = false;
        }
        return null;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
