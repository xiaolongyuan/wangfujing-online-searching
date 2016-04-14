package com.wfj.search.online.index.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.CategoryIndexPojo;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * <p>create at 16-3-26</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface CategoryEsIao {
    void upsert(CategoryIndexPojo category) throws JsonProcessingException, IndexException;

    Collection<CategoryIndexPojo> multiGet(Set<String> categoryIds) throws IOException;

    CategoryIndexPojo get(String cid);
}
