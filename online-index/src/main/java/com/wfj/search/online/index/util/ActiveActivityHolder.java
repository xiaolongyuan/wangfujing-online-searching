package com.wfj.search.online.index.util;

import com.google.common.collect.Maps;
import com.wfj.search.online.common.pojo.ActivityPojo;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>create at 16-1-13</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component
public class ActiveActivityHolder {
    private Map<String, ActivityPojo> activeActivities = Maps.newConcurrentMap();

    public Map<String, ActivityPojo> getActiveActivities() {
        return activeActivities;
    }

    public void setActiveActivities(Map<String, ActivityPojo> activeActivities) {
        this.activeActivities = activeActivities;
    }
}
