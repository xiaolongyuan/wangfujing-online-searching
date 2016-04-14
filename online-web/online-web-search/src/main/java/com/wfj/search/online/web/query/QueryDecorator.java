package com.wfj.search.online.web.query;

import com.wfj.search.online.web.common.pojo.SearchParams;
import org.apache.solr.client.solrj.SolrQuery;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface QueryDecorator {
    void decorator(SolrQuery query, SearchParams params);
}
