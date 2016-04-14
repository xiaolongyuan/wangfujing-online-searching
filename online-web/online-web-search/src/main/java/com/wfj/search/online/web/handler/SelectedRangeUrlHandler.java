package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.RangeDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SelectedRangeUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        RangeDisplayPojo selectedRange = searchParams.getSelectedRange();
        if (selectedRange != null) {
            selectedRange.setUrl(preUrl + searchParams.copy().setCurrentPage(1)
                    .setSelectedRange(null).toUrl());
        }
    }
}
