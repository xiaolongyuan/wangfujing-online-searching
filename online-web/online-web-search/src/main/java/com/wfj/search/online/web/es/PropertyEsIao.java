package com.wfj.search.online.web.es;

import com.wfj.search.online.index.pojo.PropertyIndexPojo;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface PropertyEsIao {
    PropertyIndexPojo get(String propertyId);
}
