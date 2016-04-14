package com.wfj.search.online.web.es;

import com.wfj.search.online.index.pojo.CategoryIndexPojo;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface CategoryEsIao {
    CategoryIndexPojo get(String catId);
}
