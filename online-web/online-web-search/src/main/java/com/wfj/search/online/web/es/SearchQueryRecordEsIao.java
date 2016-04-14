package com.wfj.search.online.web.es;

import com.wfj.search.online.common.pojo.SearchQueryHistoryRecord;
import com.wfj.search.online.common.pojo.SearchQueryRecord;
import com.wfj.search.online.web.pojo.PagedResult;

import java.util.List;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface SearchQueryRecordEsIao {
    void upsert(SearchQueryRecord queryRecord);

    PagedResult<SearchQueryHistoryRecord> aggregationHistory(String trackId, String prefix, String channel, int limit);

    List<String> findUUIDsByTrackIdAndQueryAndChannel(String trackId, String query, String channel);

    void markHistoryRemoved(String uuid);
}
