package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.ColorDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SelectedDisplayPojo;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SelectedColorUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        SelectedDisplayPojo<ColorDisplayPojo> selectedColors = searchParams.getSelectedColors();
        selectedColors.setUrl(preUrl + searchParams.copy().setCurrentPage(1)
                .setSelectedColors(new SelectedDisplayPojo<>()).toUrl());
    }
}
