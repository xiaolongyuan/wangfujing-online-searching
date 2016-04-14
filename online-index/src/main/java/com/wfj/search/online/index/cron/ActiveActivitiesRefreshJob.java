package com.wfj.search.online.index.cron;

import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.service.IActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * <p>create at 16-1-13</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class ActiveActivitiesRefreshJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IActivityService activityService;

    public void refreshActiveActivities() {
        Date when = new Date();
        try {
            this.activityService.freshActiveActivities(when);
        } catch (RequestException e) {
            logger.error("刷新活动状态活动失败");
        }
    }
}
