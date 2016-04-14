package com.wfj.search.online.index.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.BrandIndexPojo;

/**
 * <p>create at 16-3-26</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface BrandEsIao {
    void upsert(BrandIndexPojo brand) throws JsonProcessingException, IndexException;

    BrandIndexPojo get(String brandId);
}
