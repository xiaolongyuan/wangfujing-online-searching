package com.wfj.search.online.index.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.CommentIndexPojo;

import java.io.IOException;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface CommentEsIao {
    CommentIndexPojo get(String commentId) throws IOException;

    ScrollPage<CommentIndexPojo> startScrollOfAll() throws IndexException;

    ScrollPage<CommentIndexPojo> scroll(String scrollId, Long scrollIdTTL) throws IndexException;

    void upsert(CommentIndexPojo comment) throws IndexException, JsonProcessingException;

    void delete(String commentId);

    long countOfSpuScoreGt(String spuId, int scoreGt);

    void removeExpired(long lastVersionKeep);
}
