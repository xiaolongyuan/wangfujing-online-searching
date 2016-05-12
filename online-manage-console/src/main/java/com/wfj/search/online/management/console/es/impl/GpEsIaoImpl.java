package com.wfj.search.online.management.console.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.Gp;
import com.wfj.search.online.common.pojo.Page;
import com.wfj.search.online.common.pojo.PageImpl;
import com.wfj.search.online.management.console.es.GpEsIao;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * <p>create at 16-5-11</p>
 *
 * @author liufl
 * @since 1.0.36
 */
@Component("gpEsIao")
public class GpEsIaoImpl implements GpEsIao {
    private static final String TYPE = "gp";
    @Autowired
    private Client esClient;
    @Value("${es.config.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Page<Gp> listConfirmed(int start, int fetch) {
        SearchResponse response = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                .setSearchType(SearchType.DEFAULT)
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("confirmed", true))
                        .must(QueryBuilders.termQuery("deleted", false)))
                .setSize(fetch).setFrom(start).get();
        SearchHit[] hits = response.getHits().getHits();
        List<Gp> gps = Lists.newArrayListWithExpectedSize(hits.length);
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            if (sourceAsString != null) {
                try {
                    gps.add(this.objectMapper.readValue(sourceAsString, Gp.class));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new PageImpl<>(gps, response.getHits().getTotalHits(), fetch);
    }

    @Override
    public void index(Gp gp) {
        try {
            IndexResponse indexResponse = this.esClient.prepareIndex(this.index, TYPE, gp.getGp())
                    .setSource(objectMapper.writeValueAsString(gp)).get();
            if (!indexResponse.isCreated()) {
                throw new IllegalStateException("记录Gp失败");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void confirm(String gp) {
        GetResponse getResponse = this.esClient.prepareGet(this.index, TYPE, gp).get();
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            UpdateResponse response = this.esClient.prepareUpdate(this.index, TYPE, gp).setDoc("confirmed", true)
                    .setDetectNoop(false).setVersion(version).get();
            if (response.getVersion() <= version) {
                throw new IllegalStateException("修改失败，doc版本未变化");
            }
        }
    }

    @Override
    public void delete(String gp) {
        this.esClient.prepareDelete(this.index, TYPE, gp).get();
    }
}
