package com.wfj.search.online.web.service;

import com.wfj.search.online.web.common.pojo.BrandDisplayPojo;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IBrandService {
    BrandDisplayPojo restoreBrand(String brandId);
}
