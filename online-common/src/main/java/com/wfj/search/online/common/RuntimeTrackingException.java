package com.wfj.search.online.common;

/**
 * <p>create at 16-1-26</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class RuntimeTrackingException extends RuntimeException {
    private final TrackingException trackingException;

    public RuntimeTrackingException(TrackingException trackingException) {
        this.trackingException = trackingException;
    }

    public TrackingException getTrackingException() {
        return trackingException;
    }
}
