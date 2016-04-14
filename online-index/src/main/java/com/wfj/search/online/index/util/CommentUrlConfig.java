package com.wfj.search.online.index.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("commentUrlConfig")
public class CommentUrlConfig {
    @Value("${comment.address}")
    private String address;
    @Value("${comment.uri.listCommentsWithPage}")
    private String listCommentsWithPage;
    @Value("${comment.uri.listCommentsByIds}")
    private String listCommentsByIds;

    private static String urlListCommentsWithPage;
    private static String urlListCommentsByIds;

    @PostConstruct
    public void initUrls() {
        urlListCommentsWithPage = address + "/" + listCommentsWithPage;
        urlListCommentsByIds = address + "/" + listCommentsByIds;
    }

    public String getUrlListCommentsWithPage() {
        return urlListCommentsWithPage;
    }

    public String getUrlListCommentsByIds() {
        return urlListCommentsByIds;
    }
}
