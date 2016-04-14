package com.wfj.search.online.index.es.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.index.es.SearchQueryRecordEsIao;
import com.wfj.search.online.index.pojo.SuggestionIndexPojo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("searchQueryRecordEsIao")
public class SearchQueryRecordEsIaoImpl implements SearchQueryRecordEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "search-query-record";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
//    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<SuggestionIndexPojo> aggregateSearchQueries(String channel) {
        try {
            SearchResponse resp = this.esClient.prepareSearch(this.index).setTypes(TYPE).setSize(0)
                    .setQuery(QueryBuilders.termQuery("channel", channel))
                    .addAggregation(
                            AggregationBuilders.terms("queryCount").minDocCount(1).size(10000).field("query")
                    ).get();
            Terms queryCount = resp.getAggregations().get("queryCount");
            return queryCount.getBuckets().stream().map(bucket -> {
                String query = bucket.getKeyAsString();
                long count = bucket.getDocCount();
                SuggestionIndexPojo pojo = new SuggestionIndexPojo();
                pojo.setCk(channel + "-" + query);
                pojo.setKeyword(query);
                pojo.setFrequency(count);
                pojo.setChannel(channel);
                return pojo;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("聚集搜索查询记录失败", e);
        }
        return Lists.newArrayList();
    }
}
