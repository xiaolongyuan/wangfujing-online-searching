package com.wfj.search.online.index.service;

import com.wfj.search.online.index.pojo.SuggestionIndexPojo;
import com.wfj.search.online.index.pojo.failure.Failure;

import java.util.List;
import java.util.Optional;

/**
 * <p>create at 16-1-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISuggestionIndexService {
    Optional<Failure> save(List<SuggestionIndexPojo> suggestionIndexPojos, long version);

    Optional<Failure> removeExpired(long version);

    Optional<Failure> commit();
}
