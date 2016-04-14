package com.wfj.search.online.web.searchTask;

import com.wfj.search.online.web.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;

/**
 * <p>create at 15-11-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface SearchTask {
    /**
     * @param searchResult 搜索结果容器，不为 {@code null}
     * @param baseQuery    已组装的基础的查询对象。可能为 {@code null} 。使用时应使用其 {@link org.apache.solr.client.solrj.SolrQuery#getCopy()() getCopy()}
     */
    void doSearch(SearchResult searchResult, SolrQuery baseQuery);
}
