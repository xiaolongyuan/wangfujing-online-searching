package com.wfj.search.online.web.query;

import com.wfj.search.online.web.common.pojo.BrandDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.GroupParams;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>create at 15-11-5</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Component("justBrandFilterQueryDecorator")
public class JustBrandFilterQueryDecorator implements QueryDecorator {

    @Override
    public void decorator(SolrQuery query, SearchParams searchParams) {
        String channel = searchParams.getChannel();
        query.setQuery("*:*");
        List<BrandDisplayPojo> selectedBrands = searchParams.getSelectedBrands().getSelected();
        if (!selectedBrands.isEmpty()) {
            query.addFilterQuery("brandId:(" + StringUtils.join(
                    selectedBrands.stream().map(BrandDisplayPojo::getId)
                            .collect(Collectors.toList()).toArray(),
                    " ") + ")");
        }
        // group参数
        query
                .set(GroupParams.GROUP, true)
                .set(GroupParams.GROUP_FIELD, "spuId")
                .set(GroupParams.GROUP_TOTAL_COUNT, true)
                .set(GroupParams.GROUP_FACET, true)
                .set(GroupParams.GROUP_TRUNCATE, true);
        query
                .addFilterQuery("channels:" + channel)
                .addFilterQuery("allLevelCategoryIds_" + channel + ":*");
    }
}
