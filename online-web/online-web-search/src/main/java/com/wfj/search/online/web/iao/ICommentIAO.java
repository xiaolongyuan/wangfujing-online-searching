package com.wfj.search.online.web.iao;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * <br/>create at 16-1-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ICommentIAO {
    QueryResponse query(SolrQuery query) throws SolrSearchException;
}
