package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SortDisplayPojo;

import java.util.List;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AvailableSortUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<SortDisplayPojo> sorts = result.getSorts();
        sorts.forEach(sort -> {
            sort.setUrl(preUrl + searchParams.copy().setCurrentPage(1).setSort(sort).toUrl());
            SortDisplayPojo opposite = sort.getOpposite();
            if (opposite != null) {
                opposite.setUrl(preUrl + searchParams.copy().setCurrentPage(1).setSort(opposite).toUrl());
            }
        });
    }
}
