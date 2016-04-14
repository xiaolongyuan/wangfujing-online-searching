package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.common.pojo.SearchQueryHistoryRecord;
import com.wfj.search.online.web.es.SearchQueryRecordEsIao;
import com.wfj.search.online.web.pojo.PagedResult;
import com.wfj.search.online.web.service.ISearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>create at 16-1-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("searchHistoryService")
public class SearchHistoryServiceImpl implements ISearchHistoryService {
    @Autowired
    private SearchQueryRecordEsIao searchQueryRecordEsIao;

    @Override
    public PagedResult<SearchQueryHistoryRecord> loadHistory(String trackId, String prefix, String channel, int limit) {
        return this.searchQueryRecordEsIao.aggregationHistory(trackId, prefix, channel, limit);
    }

    @Override
    public void markHistoryRemoved(String trackId, String query, String channel) {
        while (true) {
            List<String> uuids = this.searchQueryRecordEsIao
                    .findUUIDsByTrackIdAndQueryAndChannel(trackId, query, channel);
            if (uuids == null || uuids.isEmpty()) {
                break;
            }
            for (String uuid : uuids) {
                this.searchQueryRecordEsIao.markHistoryRemoved(uuid);
            }
        }
    }
}
