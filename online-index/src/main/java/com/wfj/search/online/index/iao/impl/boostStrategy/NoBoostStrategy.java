package com.wfj.search.online.index.iao.impl.boostStrategy;

import com.wfj.search.online.index.pojo.ItemIndexPojo;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;

/**
 * 不加权
 * <p>create at 15-11-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("noBoostStrategy")
public class NoBoostStrategy implements BoostStrategy {
    @Override
    public SolrInputDocument boost(SolrClient solrClient, ItemIndexPojo item) {
        SolrInputDocument doc = solrClient.getBinder().toSolrInputDocument(item);
        doc.setDocumentBoost(this.natureBoost(item));
        return doc;
    }
}
