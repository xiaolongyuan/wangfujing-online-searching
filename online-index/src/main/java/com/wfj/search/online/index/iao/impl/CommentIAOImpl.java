package com.wfj.search.online.index.iao.impl;

import com.wfj.search.online.common.constant.CollectionNames;
import com.wfj.search.online.index.iao.ICommentIAO;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.CommentIndexPojo;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("commentIAO")
public class CommentIAOImpl implements ICommentIAO {
    private static final String COLLECTION_NAME = CollectionNames.COMMENT_COLLECTION_NAME;
    @Autowired
    private SolrClient solrClient;

    @Override
    public void save(Collection<CommentIndexPojo> array) throws IndexException {
        if(array == null || array.isEmpty()) {
            return;
        }
        try {
            this.solrClient.addBeans(COLLECTION_NAME, array);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void remove(String sid) throws IndexException {
        if(StringUtils.isBlank(sid)) {
            return;
        }
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME, "sid:" + sid);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeExpired(long versionNo) throws IndexException {
        if(versionNo == 0) {
            return;
        }
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME, "-operationSid:[" + versionNo + " TO *]");
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
