package com.wfj.search.online.web.searchTask;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * <p>create at 16-3-14</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public abstract class NewProductsAlwaysFacetSearchTaskBase {
    protected SolrQuery baseFacetUseQuery(SolrQuery baseQuery) {
        SolrQuery facetBaseQuery = baseQuery.getCopy();
        facetBaseQuery.setFilterQueries("");
        for (String fq : baseQuery.getFilterQueries()) {
            if (fq.contains("channels") || fq.contains("allLevelCategoryIds_") || fq.contains("onSellSince")) {
                facetBaseQuery.addFilterQuery(fq);
            }
        }
        return facetBaseQuery;
    }
}
