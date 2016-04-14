package com.wfj.search.online.web.iao.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.common.constant.CollectionNames;
import com.wfj.search.online.web.iao.ISuggestionIAO;
import com.wfj.search.online.web.pojo.SuggestionItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * <p>create at 16-1-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("suggestionIAO")
public class SuggestionIAOImpl implements ISuggestionIAO {
    private final static String COLLECTION_NAME = CollectionNames.SUGGESTION_COLLECTION_NAME;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SolrClient solrClient;

    @Override
    public List<SuggestionItem> prefix(String prefix, String channel, int limit) {
        if(StringUtils.isNotBlank(prefix)) {
            prefix = prefix.replaceAll("\\p{Punct}", " ").replaceAll("\\s+", "\\\\ ").toLowerCase();
        } else {
            prefix = "";
        }
        SolrQuery query = new SolrQuery("*:*")
                .addFilterQuery("channel:" + channel)
                .addFilterQuery("suggestion:" + prefix + "*")
                .addFilterQuery("-matchCount:0")
                .addFilterQuery("-suggestion:\\*")
                .addField("keyword")
                .addField("matchCount")
                .setStart(0)
                .setRows(limit)
                .addSort("frequency", SolrQuery.ORDER.desc)
                .addSort("matchCount", SolrQuery.ORDER.desc);
        List<SuggestionItem> suggestionItems = Lists.newArrayList();
        try {
            QueryResponse response = this.solrClient.query(COLLECTION_NAME, query);
            response.getResults().forEach(doc -> suggestionItems.add(new SuggestionItem() {{
                setHistory(false);
                setText((String) doc.getFieldValue("keyword"));
                setCount((Long) doc.getFieldValue("matchCount"));
            }}));
        } catch (SolrServerException | IOException e) {
            logger.error("查询建议词库失败", e);
        }
        return suggestionItems;
    }
}
