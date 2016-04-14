package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.service.ISeoService;
import org.springframework.stereotype.Service;

/**
 * <p>create at 15-12-29</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("seoService")
public class SeoServiceImpl implements ISeoService {
    @Override
    public void buildCategorySeo(CategoryDisplayPojo categoryDisplayPojo) {
        // TODO category seo Url
        categoryDisplayPojo.setUrl(categoryDisplayPojo.getUrl());
        categoryDisplayPojo.setDisplay(categoryDisplayPojo.getDisplay());
    }
}
