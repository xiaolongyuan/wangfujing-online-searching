package com.wfj.search.online.index.service;

import com.wfj.search.online.common.pojo.ActivityPojo;
import com.wfj.search.online.index.iao.RequestException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>create at 16-1-13</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IActivityService {
    Map<String, ActivityPojo> freshActiveActivities(Date when) throws RequestException;

    List<String> listClosedActivityIds(Date after, Date before) throws RequestException;
}
