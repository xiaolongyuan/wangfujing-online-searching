package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.index.pojo.PropertyValueIndexPojo;
import com.wfj.search.online.web.common.pojo.PropertyValueDisplayPojo;
import com.wfj.search.online.web.es.PropertyValueEsIao;
import com.wfj.search.online.web.service.IPropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_PROPERTY_VALUE;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("propertyValueService")
public class PropertyValueServiceImpl implements IPropertyValueService {
    @Autowired
    private PropertyValueEsIao propertyValueEsIao;

    @Override
    @Cacheable(VALUE_KEY_PROPERTY_VALUE)
    public PropertyValueDisplayPojo restorePropertyValue(String propertyValueId) {
        PropertyValueIndexPojo indexPojo = this.propertyValueEsIao.get(propertyValueId);
        if (indexPojo == null) {
            return null;
        }
        PropertyValueDisplayPojo displayPojo = new PropertyValueDisplayPojo();
        displayPojo.setId(indexPojo.getPropertyValueId());
        displayPojo.setDisplay(indexPojo.getPropertyValue());
        return displayPojo;
    }
}
