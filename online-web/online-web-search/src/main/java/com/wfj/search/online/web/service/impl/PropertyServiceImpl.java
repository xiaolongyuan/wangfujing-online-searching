package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.index.pojo.PropertyIndexPojo;
import com.wfj.search.online.web.common.pojo.PropertyDisplayPojo;
import com.wfj.search.online.web.es.PropertyEsIao;
import com.wfj.search.online.web.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_PROPERTY;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("propertyService")
public class PropertyServiceImpl implements IPropertyService {
    @Autowired
    private PropertyEsIao propertyEsIao;

    @Override
    @Cacheable(VALUE_KEY_PROPERTY)
    public PropertyDisplayPojo restoreProperty(String propertyId) {
        PropertyIndexPojo indexPojo = this.propertyEsIao.get(propertyId);
        if (indexPojo == null) {
            return null;
        }
        PropertyDisplayPojo displayPojo = new PropertyDisplayPojo();
        displayPojo.setId(indexPojo.getPropertyId());
        displayPojo.setDisplay(indexPojo.getPropertyName());
        displayPojo.setPropertyOrder(indexPojo.getPropertyOrder());
        return displayPojo;
    }
}
