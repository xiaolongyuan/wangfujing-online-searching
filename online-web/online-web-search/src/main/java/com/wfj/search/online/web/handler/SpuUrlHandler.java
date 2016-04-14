package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.*;
import com.wfj.search.online.web.service.ISearchConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>create at 15-12-30</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SpuUrlHandler extends AbstractUrlHandler {
    @Autowired
    private ISearchConfigService searchConfigService;

    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        String brandLocation = this.searchConfigService.getBrandLocation();
        result.getSuccessList().forEach(spu -> {
            spu.setUrl("#");
            spu.getColorItems().forEach(colorItem -> colorItem.setUrl("#"));
            SearchParams params = new SearchParams();
            params.setChannel(searchParams.getChannel());
            params.setCurrentPage(1);
            params.setSelectedBrands(new SelectedDisplayPojo<>());
            BrandDisplayPojo brand = new BrandDisplayPojo();
            brand.setDisplay(spu.getBrandName());
            brand.setId(spu.getBrandId());
            params.getSelectedBrands().getSelected().add(brand);
            List<CategoryDisplayPojo> leafCategories = spu.getLeafCategories();
            if (leafCategories != null && !leafCategories.isEmpty()) {
                CategoryDisplayPojo cat = leafCategories.get(0);
                params.getSelectedCategories().add(cat.getParent().getParent());
                params.getSelectedCategories().add(cat.getParent());
                params.getSelectedCategories().add(cat);
            }
            spu.setMoreUrl(brandLocation + params.toUrl());
        });
    }
}
