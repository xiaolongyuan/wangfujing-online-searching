package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SelectedDisplayPojo;
import com.wfj.search.online.web.common.pojo.StandardDisplayPojo;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SelectedStandardUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        SelectedDisplayPojo<StandardDisplayPojo> selectedStandards = searchParams.getSelectedStandards();
        selectedStandards.setUrl(preUrl + searchParams.copy().setCurrentPage(1)
                .setSelectedStandards(new SelectedDisplayPojo<>()).toUrl());
    }
}
