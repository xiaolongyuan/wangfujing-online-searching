package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.BrandDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SelectedDisplayPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <br/>create at 15-11-16
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AvailableBrandUrlHandler extends AbstractUrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(AvailableBrandUrlHandler.class);

    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<BrandDisplayPojo> availableBrands = result.getFilters().getAvailableBrands().getAvailables();
        if (availableBrands.size() > 0) {
            availableBrands.forEach(brand -> {
                SelectedDisplayPojo<BrandDisplayPojo> brands = new SelectedDisplayPojo<>();
                brands.getSelected().add(brand);
                brand.setUrl(preUrl + searchParams.copy().setCurrentPage(1).setSelectedBrands(brands).toUrl());
            });
            if (logger.isDebugEnabled()) {
                logger.debug("availableBrands.toString() is {}", availableBrands.toString());
            }
        } else {
            logger.debug("non-brand is available.");
        }
    }
}
