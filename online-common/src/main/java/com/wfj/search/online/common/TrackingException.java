package com.wfj.search.online.common;

/**
 * <p>create at 16-1-26</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class TrackingException extends Exception {
    private final String errorCode;

    public TrackingException(String errorCode) {
        this(null, errorCode);
    }

    public TrackingException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
