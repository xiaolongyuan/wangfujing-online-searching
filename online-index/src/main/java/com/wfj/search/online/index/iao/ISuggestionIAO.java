package com.wfj.search.online.index.iao;

import com.wfj.search.online.index.pojo.SuggestionIndexPojo;

import java.util.List;

/**
 * <p>create at 16-1-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISuggestionIAO {
    void save(List<SuggestionIndexPojo> suggestionIndexPojos) throws IndexException;

    void removeExpired(long version) throws IndexException;

    void commit() throws IndexException;
}
