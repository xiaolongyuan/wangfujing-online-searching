package com.wfj.search.online.web.es;

import com.wfj.search.online.index.pojo.PropertyValueIndexPojo;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface PropertyValueEsIao {
    PropertyValueIndexPojo get(String propertyValueId);
}
