package com.wfj.search.online.index.iao;

/**
 * 请求接口异常
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public class RequestException extends Exception {
    public RequestException() {
    }

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    public RequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
