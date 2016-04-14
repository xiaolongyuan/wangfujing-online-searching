package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.web.iao.ISuggestionIAO;
import com.wfj.search.online.web.pojo.PagedResult;
import com.wfj.search.online.web.pojo.SuggestionItem;
import com.wfj.search.online.web.service.ISuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>create at 16-1-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("suggestionService")
public class SuggestionServiceImpl implements ISuggestionService {
    @Autowired
    private ISuggestionIAO suggestionIAO;

    @Override
    public PagedResult<SuggestionItem> prefix(String prefix, String channel, int limit) {
        PagedResult<SuggestionItem> suggestions = new PagedResult<>();
        List<SuggestionItem> suggestionList = this.suggestionIAO.prefix(prefix, channel, limit);
        suggestions.getList().addAll(suggestionList);
        return suggestions;
    }
}
