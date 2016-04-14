package com.wfj.search.online.web.iao;

import com.wfj.search.online.web.pojo.SuggestionItem;

import java.util.List;

/**
 * <p>create at 16-1-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISuggestionIAO {
    List<SuggestionItem> prefix(String prefix, String channel, int limit);
}
