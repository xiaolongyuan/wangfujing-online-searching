package com.wfj.search.online.web.service;

import com.wfj.search.online.web.pojo.PagedResult;
import com.wfj.search.online.web.pojo.SuggestionItem;

/**
 * <p>create at 16-1-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISuggestionService {
    PagedResult<SuggestionItem> prefix(String prefix, String channel, int limit);
}
