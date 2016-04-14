package com.wfj.search.online.web.param;

import com.wfj.search.online.web.common.pojo.SearchParams;
import org.springframework.stereotype.Component;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("currentPageParamRestorer")
public class CurrentPageParamRestorer implements SearchParamRestorer<Integer> {
    @Override
    public void restore(SearchParams searchParams, Integer currentPage) {
        searchParams.setCurrentPage(currentPage);
    }
}
