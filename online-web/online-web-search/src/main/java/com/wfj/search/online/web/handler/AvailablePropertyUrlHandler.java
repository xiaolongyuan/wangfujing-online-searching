package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.*;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AvailablePropertyUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<AvailableDisplayPojo<PropertyValueDisplayPojo>> availableProperties = result.getFilters()
                .getAvailableProperties();
        availableProperties.forEach(property -> {
            PropertyDisplayPojo p = new PropertyDisplayPojo();
            p.setId(property.getId());
            p.setDisplay(property.getDisplay());
            property.getAvailables().forEach(propertyValue -> {
                p.getValues().clear();
                p.getValues().add(propertyValue);
                SearchParams copy = searchParams.copy();
                copy.getSelectedProperties().add(p);
                propertyValue.setUrl(preUrl + copy.setCurrentPage(1).toUrl());
            });
        });
    }
}
