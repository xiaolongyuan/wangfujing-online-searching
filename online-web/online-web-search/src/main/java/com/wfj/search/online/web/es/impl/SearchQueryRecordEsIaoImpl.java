package com.wfj.search.online.web.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.SearchQueryHistoryRecord;
import com.wfj.search.online.common.pojo.SearchQueryRecord;
import com.wfj.search.online.web.es.SearchQueryRecordEsIao;
import com.wfj.search.online.web.pojo.PagedResult;
import com.wfj.search.utils.es.EsUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
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
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void upsert(SearchQueryRecord queryRecord) {
        try {
            EsUtil.upsert(this.esClient, queryRecord, queryRecord.getUuid(), index, TYPE);
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        }
    }

    @Override
    public PagedResult<SearchQueryHistoryRecord> aggregationHistory(String trackId, String prefix, String channel,
            int limit) {
        PagedResult<SearchQueryHistoryRecord> pagedResult = new PagedResult<>();
        pagedResult.setFetch(limit);
        try {
            BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("trackId", trackId))
                    .must(QueryBuilders.termQuery("channel", channel))
                    .must(QueryBuilders.termQuery("removed", false));
            if (StringUtils.isNoneBlank(prefix)) {
                prefix = prefix.trim();
                query = query.must(
                        QueryBuilders.boolQuery()
                                .should(QueryBuilders.prefixQuery("query", prefix))
                                .should(QueryBuilders.prefixQuery("queryPinyin", prefix))
                                .should(QueryBuilders.prefixQuery("queryAbbre", prefix))
                                .minimumNumberShouldMatch(1)
                );
            }
            SearchResponse resp = this.esClient.prepareSearch(this.index).setTypes(TYPE).setSize(0)
                    .setQuery(query).addAggregation(
                            AggregationBuilders.terms("his").field("query").size(limit)
                                    .subAggregation(AggregationBuilders.max("lastQueryTime").field("queryTime"))
                                    .order(Terms.Order.aggregation("lastQueryTime", "value", false))
                    ).get();
            Terms his = resp.getAggregations().get("his");
            List<SearchQueryHistoryRecord> list = his.getBuckets().stream().map(bucket -> {
                SearchQueryHistoryRecord record = new SearchQueryHistoryRecord();
                record.setCount(bucket.getDocCount());
                record.setQuery(bucket.getKeyAsString());
                return record;
            }).collect(Collectors.toList());
            pagedResult.getList().addAll(list);
        } catch (Exception e) {
            logger.error("查询搜索记录失败", e);
            pagedResult.setMsg("查询搜索记录失败," + e.toString());
        }
        return pagedResult;
    }

    @Override
    public List<String> findUUIDsByTrackIdAndQueryAndChannel(String trackId, String query, String channel) {
        try {
            SearchResponse resp = this.esClient.prepareSearch(this.index).setTypes(TYPE).setSize(10000).setQuery(
                    QueryBuilders.boolQuery().must(QueryBuilders.termQuery("trackId", trackId))
                            .must(QueryBuilders.termQuery("query", query))
                            .must(QueryBuilders.termQuery("channel", channel))
                            .must(QueryBuilders.termQuery("removed", false))
            ).get();
            SearchHit[] hits = resp.getHits().hits();
            return Lists.newArrayList(hits).stream().map(SearchHit::getId).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("查询搜索记录失败", e);
            return Lists.newArrayList();
        }
    }

    @Override
    public void markHistoryRemoved(String uuid) {
        DeleteResponse resp = this.esClient.prepareDelete(this.index, TYPE, uuid).get();
        ActionWriteResponse.ShardInfo shardInfo = resp.getShardInfo();
        if (shardInfo.getFailed() == shardInfo.getTotal() && shardInfo.getTotal() > 0) {
            logger.error("所有节点均删除失败");
        }
    }
}
