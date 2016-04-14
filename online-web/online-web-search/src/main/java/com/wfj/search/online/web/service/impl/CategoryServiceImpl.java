package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.index.pojo.CategoryIndexPojo;
import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.es.CategoryEsIao;
import com.wfj.search.online.web.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_CATEGORY;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryEsIao categoryEsIao;

    @Override
    @Cacheable(VALUE_KEY_CATEGORY)
    public CategoryDisplayPojo restoreCategory(String catId) {
        CategoryIndexPojo indexPojo;
        indexPojo = this.categoryEsIao.get(catId);
        if (indexPojo == null) {
            return null;
        }
        CategoryDisplayPojo displayPojo = new CategoryDisplayPojo();
        displayPojo.setOrder(indexPojo.getOrder());
        displayPojo.setId(indexPojo.getCategoryId());
        displayPojo.setDisplay(indexPojo.getCategoryName());
        displayPojo.setParentId(indexPojo.getParentCategoryId());
        return displayPojo;
    }
}
