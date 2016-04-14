package com.wfj.search.online.index.pojo.failure;

import org.apache.commons.lang3.StringUtils;

/**
 * <br/>create at 15-12-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Failure {
    protected DataType dataType = DataType.unknown;
    private String param;
    protected FailureType failureType = FailureType.initial;
    protected String message;
    protected Throwable throwable;

    public Failure() {
    }

    public Failure(String param) {
        this.param = param;
    }

    public Failure(DataType dataType, FailureType failureType, String param) {
        this.dataType = dataType;
        this.failureType = failureType;
        this.param = param;
    }

    public Failure(DataType dataType, FailureType failureType, String param, String message, Throwable throwable) {
        this.dataType = dataType;
        this.param = param;
        this.failureType = failureType;
        this.message = message;
        this.throwable = throwable;
    }

    public boolean isSuccess() {
        return dataType == DataType.unknown || failureType == FailureType.initial || throwable == null
                || StringUtils.isBlank(param) || StringUtils.isBlank(message);
    }

    public DataType getDataType() {
        return dataType;
    }

    public Failure setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getParam() {
        return param;
    }

    public Failure setParam(String param) {
        this.param = param;
        return this;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public Failure setFailureType(FailureType failureType) {
        this.failureType = failureType;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Failure setMessage(String message) {
        this.message = message;
        return this;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Failure setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder("{");
        msg.append("参数:").append(param);
        if (!DataType.unknown.equals(dataType)) {
            msg.append(",数据类型:").append(dataType);
        }
        if (!FailureType.initial.equals(failureType)) {
            msg.append(",错误类型:").append(failureType);
        }
        if (StringUtils.isNotBlank(message)) {
            msg.append(",异常信息:").append(message);
        }
        if (throwable != null) {
            msg.append(",异常栈信息:").append(throwable.getMessage());
        }
        msg.append("}");
        return msg.toString();
    }
}
