package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.web.iao.ICommentIAO;
import com.wfj.search.online.web.pojo.SearchResult;
import com.wfj.search.online.web.service.ICommentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SEARCH_COMMENT;

/**
 * <br/>create at 16-1-12
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("commentService")
public class CommentServiceImpl implements ICommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    @Autowired
    private ICommentIAO commentIAO;

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_COMMENT)
    public SearchResult<Map<String, Object>> query(int start, int fetch, String skuId) {
        if (start < 0) {
            start = 0;
        }
        if (fetch < 0) {
            fetch = 0;
        }
        if (StringUtils.isBlank(skuId)) {
            skuId = "*";
        }
        SolrQuery query = new SolrQuery()
                .setQuery("skuId:" + skuId)
                .setSort("createTime", SolrQuery.ORDER.desc)
                .setStart(start)
                .setRows(fetch)
                .setFields("itemId", "skuId", "spuId", "memberId", "memberName", "memberLevel", "memberImg",
                        "title", "content", "userScore", "createTime", "mind", "pics");
        SearchResult<Map<String, Object>> searchResult = new SearchResult<>();
        try {
            QueryResponse response = commentIAO.query(query);
            if (response == null) {
                RuntimeException e = new RuntimeException("查询用户评论失败");
                logger.error("查询用户评论出现异常情况", e);
                throw e;
            }
            SolrDocumentList results = response.getResults();
            results.forEach(doc -> {
                doc.setField("createTime", doc.getFieldValue("createTime"));
                doc.setField("orderCreatedTime", doc.getFieldValue("orderCreatedTime"));
                Collection<Object> pics = doc.getFieldValues("pics");
                if (pics == null) {
                    doc.setField("pics", Collections.emptyList());
                }
                Object memberName = doc.getFieldValue("memberName");
                doc.setField("memberName", hideName(memberName == null ? "" : memberName.toString()));
            });
            searchResult.getList().addAll(results);
            long total = results.getNumFound();
            searchResult.setTotal(total);
            searchResult.setPageSize((int) (total + fetch - 1) / fetch);
        } catch (Exception e) {
            logger.error("查询用户评论失败", e);
        }
        return searchResult;
    }

    private String hideName(String memberName) {
        String hideMsg = "***";
        if (StringUtils.isBlank(memberName)) {
            return hideMsg;
        }
        switch (memberName.length()) {
            case 1:
            case 2: {
                return memberName.substring(0, 1) + hideMsg;
            }
            default: {
                return memberName.substring(0, 1) + hideMsg + memberName.substring(memberName.length() - 1);
            }
        }
    }
}
