package com.wfj.search.online.web.iao;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SolrSearchException extends Exception {
    public SolrSearchException() {
    }

    public SolrSearchException(String message) {
        super(message);
    }

    public SolrSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SolrSearchException(Throwable cause) {
        super(cause);
    }

    public SolrSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
