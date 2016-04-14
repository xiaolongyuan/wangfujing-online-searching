package com.wfj.search.online.web.controller;

import com.google.common.collect.Sets;
import com.wfj.search.online.common.pojo.SearchQueryHistoryRecord;
import com.wfj.search.online.web.pojo.PagedResult;
import com.wfj.search.online.web.pojo.SuggestionItem;
import com.wfj.search.online.web.service.ISearchConfigService;
import com.wfj.search.online.web.service.ISearchHistoryService;
import com.wfj.search.online.web.service.ISuggestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * <p>create at 16-1-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/auto-completion")
public class AutoCompletionController {
    @Autowired
    private ISearchHistoryService searchHistoryService;
    @Autowired
    private ISearchConfigService searchConfigService;
    @Autowired
    private ISuggestionService suggestionService;

    @RequestMapping("/hh")
    @ResponseBody
    public PagedResult<SuggestionItem> historyAndHotKeyWords(
            @CookieValue(value = "wfj-st", required = false) String trackId,
            @RequestParam(value = "prefix", required = false) String prefix,
            @RequestParam(value = "channel", required = false) String channel) {
        if (StringUtils.isBlank(channel)) {
            channel = this.searchConfigService.getChannel();
        }
        PagedResult<SuggestionItem> suggestionItems = new PagedResult<>();
        int limit = 10;
        PagedResult<SearchQueryHistoryRecord> historyRecords = this.searchHistoryService
                .loadHistory(trackId, prefix, channel, limit);
        suggestionItems.setFetch(limit);
        suggestionItems.setTotal(historyRecords.getList().size());
        Set<String> historySet = Sets.newHashSet();
        for (SearchQueryHistoryRecord record : historyRecords.getList()) {
            suggestionItems.getList().add(new SuggestionItem() {{
                setText(record.getQuery());
                setHistory(true);
            }});
            historySet.add(record.getQuery().toLowerCase());
        }
        if (historySet.isEmpty() || StringUtils.isNotBlank(prefix)) {
            PagedResult<SuggestionItem> suggestions = this.suggestionService.prefix(prefix, channel, limit);
            for (SuggestionItem suggestionItem : suggestions.getList()) {
                if (suggestionItems.getList().size() > limit) {
                    break;
                }
                if (historySet.contains(suggestionItem.getText().toLowerCase())) {
                    continue;
                }
                suggestionItems.getList().add(suggestionItem);
                suggestionItems.setTotal(suggestionItems.getTotal() + 1);
                historySet.add(suggestionItem.getText().toLowerCase());
            }
        }
        return suggestionItems;
    }
}
