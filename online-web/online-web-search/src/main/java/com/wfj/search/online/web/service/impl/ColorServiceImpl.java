package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.index.pojo.ColorIndexPojo;
import com.wfj.search.online.web.common.pojo.ColorDisplayPojo;
import com.wfj.search.online.web.es.ColorEsIao;
import com.wfj.search.online.web.service.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_COLOR;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("colorService")
public class ColorServiceImpl implements IColorService {
    @Autowired
    private ColorEsIao colorEsIao;

    @Override
    @Cacheable(VALUE_KEY_COLOR)
    public ColorDisplayPojo restoreColor(String colorId) {
        ColorIndexPojo indexPojo;
        indexPojo = this.colorEsIao.get(colorId);
        if (indexPojo == null) {
            return null;
        }
        ColorDisplayPojo displayPojo = new ColorDisplayPojo();
        displayPojo.setId(indexPojo.getColorId());
        displayPojo.setDisplay(indexPojo.getColorName());
        return displayPojo;
    }
}
