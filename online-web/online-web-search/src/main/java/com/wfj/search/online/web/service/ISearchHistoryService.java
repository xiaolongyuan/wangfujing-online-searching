package com.wfj.search.online.web.service;

import com.wfj.search.online.common.pojo.SearchQueryHistoryRecord;
import com.wfj.search.online.web.pojo.PagedResult;

/**
 * <p>create at 16-1-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISearchHistoryService {
    PagedResult<SearchQueryHistoryRecord> loadHistory(String trackId, String prefix, String channel, int limit);

    void markHistoryRemoved(String trackId, String query, String channel);
}
