package com.wfj.search.online.index.iao.impl;

import com.wfj.search.online.common.constant.CollectionNames;
import com.wfj.search.online.index.iao.ISuggestionIAO;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.SuggestionIndexPojo;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * <p>create at 16-1-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("suggestionIAO")
public class SuggestionIAOImpl implements ISuggestionIAO {
    private static final String COLLECTION_NAME = CollectionNames.SUGGESTION_COLLECTION_NAME;
    @Autowired
    private SolrClient solrClient;

    @Override
    public void save(List<SuggestionIndexPojo> suggestionIndexPojos) throws IndexException {
        if (suggestionIndexPojos == null || suggestionIndexPojos.isEmpty()) {
            return;
        }
        try {
            this.solrClient.addBeans(COLLECTION_NAME, suggestionIndexPojos);
        } catch (IOException | SolrServerException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeExpired(long version) throws IndexException {
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME, "-operationSid:[" + version + " TO *]");
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void commit() throws IndexException {
        try {
            this.solrClient.commit(COLLECTION_NAME, false, true);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }
}
