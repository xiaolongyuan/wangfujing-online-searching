package com.wfj.search.online.web.es;

import com.wfj.search.online.index.pojo.BrandIndexPojo;

/**
 * <p>create at 16-3-26</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface BrandEsIao {
    BrandIndexPojo get(String brandId);
}
