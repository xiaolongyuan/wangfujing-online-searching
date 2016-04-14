package com.wfj.search.online.web.param;

import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("catIdPathParamRestorer")
public class CatIdPathParamRestorer implements SearchParamRestorer<List<String>> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ICategoryService categoryService;

    @Override
    public void restore(SearchParams searchParams, List<String> catIdPath) {
        if (catIdPath == null || catIdPath.size() != 3) {
            throw new IllegalArgumentException("分类编码路径参数非法");
        }
        try {
            catIdPath.stream().filter(catId -> StringUtils.isNotBlank(catId) && !"0".equals(catId.trim()))
                    .forEach(catId -> {
                        CategoryDisplayPojo categoryDisplayPojo = this.categoryService.restoreCategory(catId);
                        if (categoryDisplayPojo != null) {
                            String parentId = categoryDisplayPojo.getParentId();
                            if (StringUtils.isNotBlank(parentId) && !"0".equals(parentId.trim())) {
                                // 有父级ID
                                CategoryDisplayPojo parent = this.categoryService
                                        .restoreCategory(parentId);
                                if (parent != null) {
                                    if (StringUtils.isBlank(parent.getParentId()) || "0"
                                            .equals(parent.getParentId().trim())) {
                                        // 有父级记录但它是假root
                                        categoryDisplayPojo.setRoot(true);
                                    }
                                } else {
                                    // 但没有父级记录（索引时过滤了假root）
                                    categoryDisplayPojo.setRoot(true);
                                }
                                searchParams.getSelectedCategories().add(categoryDisplayPojo);
                            }
                        }
                    });
            if (!searchParams.getSelectedCategories().isEmpty()) {
                if (!searchParams.getSelectedCategories().get(0).isRoot()) {
                    throw new IllegalArgumentException("第一个分类ID不是root分类");
                }
            }
            for (int i = 0; i < searchParams.getSelectedCategories().size() - 1; i++) {
                CategoryDisplayPojo p = searchParams.getSelectedCategories().get(i);
                CategoryDisplayPojo c = searchParams.getSelectedCategories().get(i + 1);
                if (!p.getId().equals(c.getParentId())) {
                    throw new IllegalArgumentException("分类" + p.getId() + "不是分类" + c.getId() + "的父级分类");
                }
            }
        } catch (Exception e) {
            String msg = "恢复分类失败";
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
