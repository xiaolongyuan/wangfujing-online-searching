package com.wfj.search.online.web.service;

import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ICategoryService {
    CategoryDisplayPojo restoreCategory(String catId);
}
