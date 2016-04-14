package com.wfj.search.online.index.util;

/**
 * <p>create at 15-10-19</p>
 *
 * @author liufl
 * @since 1.0.0
 * @deprecated 使用 {@link com.wfj.search.online.index.pojo.failure.Failure} 及其子类
 */
@Deprecated
public class IndexFailure {
    protected String message;
    protected Throwable exception;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
