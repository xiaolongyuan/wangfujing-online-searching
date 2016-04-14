package com.wfj.search.online.index.iao;

/**
 * 数据导入异常
 * <p>create at 15-7-11</p>
 *
 * @author liufl
 * @since 1.0.0
 * @deprecated 调用接口异常使用 {@link RequestException} 替代
 */
@Deprecated
@SuppressWarnings("ALL")
public class PcmRequestException extends Exception {
    public PcmRequestException() {
    }

    public PcmRequestException(String message) {
        super(message);
    }

    public PcmRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public PcmRequestException(Throwable cause) {
        super(cause);
    }

    public PcmRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
