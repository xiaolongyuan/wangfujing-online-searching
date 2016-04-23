package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wfj.search.online.index.es.CommentEsIao;
import com.wfj.search.online.index.es.ScrollPage;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.CommentIndexPojo;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("commentEsIao")
public class CommentEsIaoImpl implements CommentEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "comment";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CommentIndexPojo get(String commentId) throws IOException {
        try {
            return EsUtil.get(esClient, commentId, index, TYPE, CommentIndexPojo.class);
        } catch (Exception e) {
            if (e.getMessage().contains("BLANK")) {
                return null;
            }
            logger.error("GET评论[{}]信息失败", commentId, e);
        }
        return null;
    }

    @Override
    public ScrollPage<CommentIndexPojo> startScrollOfAll() throws IndexException {
        SearchResponse scrollResp;
        try {
            scrollResp = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.matchAllQuery()).setSize(100)
                    .setScroll(new TimeValue(60000)).get();
        } catch (IllegalStateException e) {
            throw new IndexException("创建scroll失败，操作被中断", e);
        } catch (RuntimeException e) {
            throw new IndexException("创建scroll失败，" + e.getMessage(), e);
        }
        ScrollPage<CommentIndexPojo> scrollPage = new ScrollPage<>();
        scrollPage.setTotal(scrollResp.getHits().totalHits());
        scrollPage.setScrollId(scrollResp.getScrollId());
        scrollPage.setScrollIdTTL(60000L);
        SearchHit[] hits = scrollResp.getHits().getHits();
        if (hits.length > 0) {
            scrollPage.setList(Lists.newArrayListWithExpectedSize(hits.length));
            for (SearchHit hit : hits) {
                try {
                    String source = hit.getSourceAsString();
                    if (source != null) {
                        scrollPage.getList()
                                .add(this.objectMapper.readValue(source, CommentIndexPojo.class));
                    }
                } catch (IOException e) {
                    logger.error("JSON反序列化失败", e);
                    throw new IndexException("从JSON反序列化评论信息失败", e);
                }
            }
        }
        return scrollPage;
    }

    @Override
    public ScrollPage<CommentIndexPojo> scroll(String scrollId, Long scrollIdTTL) throws IndexException {
        SearchResponse scrollResp;
        try {
            scrollResp = this.esClient.prepareSearchScroll(scrollId)
                    .setScroll(new TimeValue(scrollIdTTL)).get();
        } catch (IllegalStateException e) {
            throw new IndexException("执行scroll失败，操作被中断", e);
        } catch (RuntimeException e) {
            throw new IndexException("执行scroll失败，" + e.getMessage(), e);
        }
        ScrollPage<CommentIndexPojo> scrollPage = new ScrollPage<>();
        scrollPage.setTotal(scrollResp.getHits().getTotalHits());
        scrollPage.setScrollId(scrollResp.getScrollId());
        scrollPage.setScrollIdTTL(scrollIdTTL);
        SearchHit[] hits = scrollResp.getHits().getHits();
        if (hits.length > 0) {
            scrollPage.setList(Lists.newArrayListWithExpectedSize(hits.length));
            for (SearchHit hit : hits) {
                try {
                    String source = hit.getSourceAsString();
                    if (source != null) {
                        scrollPage.getList()
                                .add(this.objectMapper.readValue(source, CommentIndexPojo.class));
                    }
                } catch (IOException e) {
                    logger.error("JSON反序列化失败", e);
                    throw new IndexException("从JSON反序列化评论信息失败", e);
                }
            }
        }
        return scrollPage;
    }

    @Override
    public void upsert(CommentIndexPojo comment) throws IndexException, JsonProcessingException {
        try {
            EsUtil.upsert(this.esClient, comment, comment.getCommentId(), index, TYPE);
        } catch (IllegalStateException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void delete(String commentId) {
        this.esClient.prepareDelete(this.index, TYPE, commentId).get();
    }

    @Override
    public long countOfSpuScoreGt(String spuId, int scoreGt) {
        try {
            SearchResponse resp = this.esClient.prepareSearch(this.index).setTypes(TYPE).setSize(0)
                    .setQuery(
                            QueryBuilders.boolQuery().must(QueryBuilders.termQuery("spuId", spuId))
                                    .must(QueryBuilders.rangeQuery("userScore").gt(scoreGt))
                    ).get();
            return resp.getHits().getTotalHits();
        } catch (Exception e) {
            logger.error("查询评论ES失败", e);
        }
        return 0;
    }

    @Override
    public void removeExpired(long lastVersionKeep) {
        while (true) {
            SearchResponse resp = this.esClient.prepareSearch(this.index).setTypes(TYPE).setSize(10000)
                    .setQuery(QueryBuilders.rangeQuery("operationSid").lt(lastVersionKeep)).get();
            if (resp.getHits().getTotalHits() == 0) {
                break;
            }
            SearchHit[] hits = resp.getHits().getHits();
            for (SearchHit hit : hits) {
                this.delete(hit.getId());
            }
        }
    }
}
