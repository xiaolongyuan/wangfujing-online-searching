package com.wfj.search.online.web.handler;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.service.ISeoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public class AvailableCategoryUrlHandler extends AbstractUrlHandler {
    @Autowired
    private ISeoService seoUrlService;

    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<CategoryDisplayPojo> queryMatchedCategoryTree = result.getFilters().getQueryMatchedCategoryTree();
        List<CategoryDisplayPojo> parents = Lists.newArrayList();
        this._handle(preUrl, searchParams, parents, queryMatchedCategoryTree);
        List<CategoryDisplayPojo> categoryTree = result.getFilters().getCategoryTree();
        parents = Lists.newArrayList();
        this._handle(preUrl, searchParams, parents, categoryTree);
    }

    private void _handle(String preUrl, SearchParams searchParams, List<CategoryDisplayPojo> parents,
            List<CategoryDisplayPojo> list) {
        for (CategoryDisplayPojo cat : list) {
            if (parents.size() < 3) {
                SearchParams copy = searchParams.copy();
                copy.getSelectedCategories().clear();
                copy.getSelectedCategories().addAll(parents);
                copy.getSelectedCategories().add(cat);
                cat.setUrl(preUrl + copy.setCurrentPage(1).toUrl());
                if (cat.isRoot()) {
                    seoUrlService.buildCategorySeo(cat);
                }
                List<CategoryDisplayPojo> children = cat.getChildren();
                if (children != null && !children.isEmpty()) {
                    List<CategoryDisplayPojo> _parents = Lists.newArrayList(parents);
                    _parents.add(cat);
                    this._handle(preUrl, searchParams, _parents, children);
                }
            } else {
                cat.setUrl("#");
            }
        }
    }
}
