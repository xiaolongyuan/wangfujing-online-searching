package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.BrandDisplayPojo;
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
public class SelectedBrandUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        SelectedDisplayPojo<BrandDisplayPojo> selectedBrands = searchParams.getSelectedBrands();
        SearchParams copy = searchParams.copy();
        copy.getSelectedBrands().getSelected().clear();
        selectedBrands.setUrl(preUrl + copy.setCurrentPage(1).toUrl());
    }
}
