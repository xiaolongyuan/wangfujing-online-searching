package com.wfj.search.online.index.pojo;

/**
 * <br/>create at 15-12-4
 *
 * @author liuxh
 * @since 1.0.0
 */
public class IndexResultSet {
    private Status status = Status.initial;
    private final StringBuffer comment = new StringBuffer();
    private Throwable throwable;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StringBuffer getComment() {
        return comment;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public static enum Status {
        initial,
        success,
        failure
    }
}
