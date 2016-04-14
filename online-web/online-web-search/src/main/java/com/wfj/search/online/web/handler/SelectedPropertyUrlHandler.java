package com.wfj.search.online.web.handler;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.PropertyDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;

import java.util.List;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SelectedPropertyUrlHandler extends AbstractUrlHandler {
    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<PropertyDisplayPojo> selectedProperties = searchParams.getSelectedProperties();
        selectedProperties.forEach(property->{
            List<PropertyDisplayPojo> properties = Lists.newArrayList(selectedProperties);
            properties.remove(property);
            property.setUrl(preUrl + searchParams.copy().setCurrentPage(1).setSelectedProperties(properties).toUrl());
        });
    }
}
