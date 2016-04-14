package com.wfj.search.online.web.param;

import com.wfj.search.online.web.common.pojo.SearchParams;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface SearchParamRestorer<T> {
    void restore(SearchParams searchParams, T param) throws RuntimeException;
}
