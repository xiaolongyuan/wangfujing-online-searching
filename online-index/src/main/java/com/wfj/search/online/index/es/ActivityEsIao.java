package com.wfj.search.online.index.es;

import com.wfj.search.online.common.pojo.ActivityPojo;

/**
 * <p>create at 16-3-26</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface ActivityEsIao {
    ActivityPojo get(String activeId);

    void upsert(ActivityPojo activityPojo);
}
