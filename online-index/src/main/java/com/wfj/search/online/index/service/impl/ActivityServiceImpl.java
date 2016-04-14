package com.wfj.search.online.index.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.common.pojo.ActivityPojo;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.service.IActivityService;
import com.wfj.search.online.index.util.ActiveActivityHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>create at 16-1-13</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("activityService")
public class ActivityServiceImpl implements IActivityService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ActiveActivityHolder activeActivityHolder;
    @Autowired
    private IPcmRequester pcmRequester;

    @Override
    public Map<String, ActivityPojo> freshActiveActivities(Date when) throws RequestException {
        int fetch = 200;
        int count = pcmRequester.countActiveActivities(when);
        int pages = (count + fetch - 1) / fetch;
        Map<String, ActivityPojo> activeActivities = Maps.newConcurrentMap();
        for (int i = 0; i < pages; i++) {
            List<ActivityPojo> activityPojos = pcmRequester.listActiveActivities(when, i * fetch, fetch);
            activityPojos.forEach(activity -> activeActivities.put(activity.getActiveId(), activity));
        }
        activeActivityHolder.setActiveActivities(activeActivities);
        return activeActivityHolder.getActiveActivities();
    }

    @Override
    public List<String> listClosedActivityIds(Date after, Date before) throws RequestException {
        int fetch = 200;
        List<String> ids = Lists.newArrayList();
        int count = pcmRequester.countClosedActivities(after, before);
        int pages = (count + fetch - 1) / fetch;
        for (int i = 0; i < pages; i++) {
            List<String> _ids = pcmRequester.listClosedActivities(after, before, i * fetch, fetch);
            ids.addAll(_ids);
        }
        return ids;
    }
}
