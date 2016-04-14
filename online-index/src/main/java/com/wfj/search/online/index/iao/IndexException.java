package com.wfj.search.online.index.iao;

/**
 * 创建索引异常
 * <br/>create at 15-7-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class IndexException extends Exception {
    public IndexException() {
    }

    public IndexException(String message) {
        super(message);
    }

    public IndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexException(Throwable cause) {
        super(cause);
    }

    public IndexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
