package com.wfj.search.online.web.service;

import com.wfj.search.online.web.common.pojo.PropertyValueDisplayPojo;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IPropertyValueService {
    PropertyValueDisplayPojo restorePropertyValue(String propertyValueId);
}
