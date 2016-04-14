package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SelectedDisplayPojo;
import com.wfj.search.online.web.common.pojo.StandardDisplayPojo;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AvailableStandardUrlHandler extends AbstractUrlHandler{
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<StandardDisplayPojo> availableStandards = result.getFilters().getAvailableStandards().getAvailables();
        availableStandards.forEach(standard->{
            SelectedDisplayPojo<StandardDisplayPojo> standards = new SelectedDisplayPojo<>();
            standards.getSelected().add(standard);
            standard.setUrl(preUrl + searchParams.copy().setCurrentPage(1).setSelectedStandards(standards).toUrl());
        });
    }
}
