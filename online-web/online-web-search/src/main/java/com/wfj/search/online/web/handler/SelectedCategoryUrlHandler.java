package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.service.ISeoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>create at 15-12-29</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SelectedCategoryUrlHandler extends AbstractUrlHandler {
    @Autowired
    private ISeoService seoUrlService;

    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<CategoryDisplayPojo> selectedCategories = searchParams.getSelectedCategories();
        for (int i = 0; i < selectedCategories.size(); i++) {
            CategoryDisplayPojo cat = selectedCategories.get(i);
            SearchParams copy = searchParams.copy();
            copy.getSelectedCategories().clear();
            copy.getSelectedCategories().addAll(selectedCategories.subList(0, i + 1));
            cat.setUrl(preUrl + copy.setCurrentPage(1).toUrl());
            if (cat.isRoot()) {
                seoUrlService.buildCategorySeo(cat);
            }
        }
    }
}
