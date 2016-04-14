package com.wfj.search.online.index.iao.impl.boostStrategy;

import com.wfj.search.online.index.pojo.ItemIndexPojo;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

/**
 * Solr文档索引加权策略
 * <p>create at 15-11-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface BoostStrategy {
    SolrInputDocument boost(SolrClient solrClient, ItemIndexPojo item);

    default Float natureBoost(ItemIndexPojo item) {
        return 1F;
    }
}
