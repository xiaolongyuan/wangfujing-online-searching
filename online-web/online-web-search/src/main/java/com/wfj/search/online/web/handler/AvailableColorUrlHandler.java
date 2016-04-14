package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.ColorDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SelectedDisplayPojo;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AvailableColorUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<ColorDisplayPojo> availableColors = result.getFilters().getAvailableColors().getAvailables();
            availableColors.forEach(color -> {
                SelectedDisplayPojo<ColorDisplayPojo> selectedColors = new SelectedDisplayPojo<>();
                selectedColors.getSelected().add(color);
                color.setUrl(preUrl + searchParams.copy().setCurrentPage(1).setSelectedColors(selectedColors).toUrl());
            });
    }
}
