package com.wfj.search.online.index.pojo.failure;

/**
 * <br/>create at 16-1-11
 *
 * @author liuxh
 * @since 1.0.0
 */
public class NoFailure extends Failure {
    public NoFailure() {
    }

    public NoFailure(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
