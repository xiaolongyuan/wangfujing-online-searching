package com.wfj.search.online.web.param;

/**
 * 无效的搜索参数
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class InvalidSearchParamException extends Exception {
    public InvalidSearchParamException() {
    }

    public InvalidSearchParamException(String message) {
        super(message);
    }

    public InvalidSearchParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSearchParamException(Throwable cause) {
        super(cause);
    }

    public InvalidSearchParamException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
