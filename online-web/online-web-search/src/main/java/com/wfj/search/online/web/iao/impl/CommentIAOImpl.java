package com.wfj.search.online.web.iao.impl;

import com.wfj.search.online.common.constant.CollectionNames;
import com.wfj.search.online.web.iao.ICommentIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <br/>create at 16-1-12
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("commentIAO")
public class CommentIAOImpl implements ICommentIAO {
    private static final Logger logger = LoggerFactory.getLogger(CommentIAOImpl.class);
    private final static String COLLECTION_NAME = CollectionNames.COMMENT_COLLECTION_NAME;
    @Autowired
    private SolrClient solrClient;

    @Override
    public QueryResponse query(SolrQuery query) throws SolrSearchException {
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, query);
        } catch (SolrServerException | IOException e) {
            logger.error("查询用户评论过程失败", e);
            throw new SolrSearchException(e);
        }
        return response;
    }
}
