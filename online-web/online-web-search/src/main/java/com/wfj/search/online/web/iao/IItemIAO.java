package com.wfj.search.online.web.iao;

import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;
import com.wfj.search.online.web.common.pojo.PropertyDisplayPojo;
import com.wfj.search.online.web.common.pojo.RangeDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.List;
import java.util.Map;

/**
 * <p>create at 15-11-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IItemIAO {
    List<FacetField.Count> facetField(SolrQuery query, String field) throws SolrSearchException;

    List<RangeDisplayPojo> facetPriceRange(SolrQuery query, IntervalContentPojo rule,
            List<IntervalDetailPojo> ruleDetails) throws SolrSearchException;

    Map<String, List<FacetField.Count>> facetPropertyValueIds(SolrQuery query, List<PropertyDisplayPojo> properties,
            String channel)
            throws SolrSearchException;

    void query(SolrQuery query, SearchResult searchResult) throws SolrSearchException;

    QueryResponse query(SolrQuery query) throws SolrSearchException;

    void spellCheckSuggest(SolrQuery query, SearchResult searchResult) throws SolrSearchException;
}
