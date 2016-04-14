package com.wfj.search.online.web.handler;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SelectedDisplayPojo;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public class AllClearUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        searchParams.setRemoveAllNoneCategoryParamUrl(preUrl + searchParams.copy()
                .setCurrentPage(1)
                .setSelectedBrands(new SelectedDisplayPojo<>())
                .setSelectedRange(null)
                .setSelectedStandards(new SelectedDisplayPojo<>())
                .setSelectedColors(new SelectedDisplayPojo<>())
                .setSelectedProperties(Lists.newArrayList())
                .setSelectedTags(new SelectedDisplayPojo<>())
                .toUrl());
    }
}
