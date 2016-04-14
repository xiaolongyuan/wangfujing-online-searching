package com.wfj.search.online.web.param;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.BrandDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.service.IBrandService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("brandIdParamRestorer")
public class BrandIdsParamRestorer implements SearchParamRestorer<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IBrandService brandService;

    @Override
    public void restore(SearchParams searchParams, String brandIds) {
        if (StringUtils.isBlank(brandIds) || "0".equals(brandIds.trim())) {
            return;
        }
        List<String> _brandIds = Lists.newArrayList();
        for (String brandId : brandIds.split("_")) {
            if (StringUtils.isNotBlank(brandId) && !"0".equals(brandId)) {
                _brandIds.add(brandId);
            }
        }
        if (_brandIds.isEmpty()) {
            return;
        }
        try {
            _brandIds.stream().filter(brandId -> StringUtils.isNotBlank(brandId) && !"0".equals(brandId.trim()))
                    .forEach(brandId -> {
                        BrandDisplayPojo brandDisplayPojo = this.brandService.restoreBrand(brandId);
                        if (brandDisplayPojo != null) {
                            searchParams.getSelectedBrands().getSelected().add(brandDisplayPojo);
                        }
                    });
        } catch (Exception e) {
            String msg = "恢复品牌失败";
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
