package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.index.pojo.BrandIndexPojo;
import com.wfj.search.online.web.common.pojo.BrandDisplayPojo;
import com.wfj.search.online.web.es.BrandEsIao;
import com.wfj.search.online.web.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_BRAND;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("brandService")
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private BrandEsIao brandEsIao;

    @Override
    @Cacheable(VALUE_KEY_BRAND)
    public BrandDisplayPojo restoreBrand(String brandId) {
        BrandIndexPojo indexPojo;
        indexPojo = this.brandEsIao.get(brandId);
        if (indexPojo == null) {
            return null;
        }
        BrandDisplayPojo displayPojo = new BrandDisplayPojo();
        displayPojo.setId(indexPojo.getBrandId());
        displayPojo.setDisplay(indexPojo.getBrandName());
        displayPojo.setDesc(indexPojo.getBrandDesc());
        displayPojo.setLogo(indexPojo.getBrandLogo());
        displayPojo.setPicture(indexPojo.getBrandPicture());
        return displayPojo;
    }
}
