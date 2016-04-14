package com.wfj.search.online.index.operation;

import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.service.IEsService;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.util.record.pojo.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 商品价格变化抓取任务
 * <p>create at 16-1-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("activeUpdateOperation")
public class ActivitiesUpdateOperation implements IOperation<Void> {
    private boolean running = false;
    @Value("${index.config.updateActivityDuration}")
    private Long updateActivityDuration;
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;

    public synchronized Void operate(Operation opRecord)
            throws IndexException, RequestException, InterruptedException {
        this.running = true;
        long currentTimeMillis = System.currentTimeMillis();
        Date before = new Date(currentTimeMillis);
        Date after = new Date(currentTimeMillis - this.updateActivityDuration);
        try {
            StringBuilder msg = new StringBuilder();
            this.esService.updateActivities(after, before, Long.parseLong(opRecord.getSid()))
                    .ifPresent(failure -> msg.append(failure.toString()));
            this.indexService.indexNewerFromEs(Long.valueOf(opRecord.getSid())).ifPresent(
                    failure -> msg.append(failure.toString()));
            String comment = opRecord.getComment();
            if (StringUtils.isBlank(comment)) {
                opRecord.setComment(msg.toString());
            } else {
                if (comment.endsWith("\n")) {
                    opRecord.setComment(comment + msg);
                } else {
                    opRecord.setComment(comment + '\n' + msg);
                }
            }
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
