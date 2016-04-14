package com.wfj.search.online.index.pojo.failure;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * <br/>create at 15-12-29
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public class MultiFailure extends Failure {
    private static final Logger logger = LoggerFactory.getLogger(MultiFailure.class);
    private volatile long total = 0;
    private volatile long success = 0;
    private volatile long fail = 0;
    private final Set<Failure> failures = Collections.synchronizedSet(new HashSet<>());

    @Override
    public boolean isSuccess() {
        return success > 0;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSuccess() {
        return success;
    }

    public void setSuccess(long success) {
        this.success = success;
    }

    public MultiFailure addSuccess(long success) {
        this.success += success;
        return this;
    }

    public long getFail() {
        return fail;
    }

    public void setFail(long fail) {
        this.fail = fail;
    }

    public MultiFailure addFail(long fail) {
        this.fail += fail;
        return this;
    }

    public Set<Failure> getFailures() {
        return failures;
    }

    public MultiFailure addFailure(Failure failure) {
        if (DataType.unknown.equals(failure.getDataType())) {
            logger.warn("数据类型未知");
        }
        if (FailureType.initial.equals(failure.getFailureType())) {
            logger.warn("错误类型未知");
        }
        this.failures.add(failure);
        return this;
    }

    public MultiFailure merge(MultiFailure other) {
        if (other == null) {
            return this;
        }
        this.total += other.total;
        this.success += other.success;
        this.fail += other.fail;
        this.failures.addAll(other.failures);
        return this;
    }

    public MultiFailure merge(Failure failure) {
        if (failure == null) {
            return this;
        }
        this.failures.add(failure);
        return this;
    }

    public Optional<Failure> toOptional() {
        if (this.failures.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this);
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("记录总数：").append(total).append("\n");
        msg.append("成功总数：").append(success).append("\n");
        if (failures.isEmpty()) {
            return msg.toString();
        }
        Map<FailureType, List<Failure>> failureTypeMap = failures.stream().collect(groupingBy(Failure::getFailureType));
        Set<FailureType> failureTypeSet = failureTypeMap.keySet();
        for (FailureType ft : failureTypeSet) {
            List<Failure> typeList = failureTypeMap.get(ft);
            if (typeList.isEmpty()) {
                continue;
            }
            msg.append("\n错误类型：[").append(ft);
            Map<DataType, List<Failure>> dataTypeMap = typeList.stream().collect(groupingBy(Failure::getDataType));
            Set<DataType> dataTypeSet = dataTypeMap.keySet();
            for (DataType dt : dataTypeSet) {
                List<Failure> list = dataTypeMap.get(dt);
                if (!list.isEmpty()) {
                    msg.append(dt).append(":").append(list);
                }
            }
            msg.append("]");
        }
        if (StringUtils.isBlank(message)) {
            msg.append("\n异常信息:").append(message);
        }
        if (throwable != null) {
            msg.append("\n异常信息：").append(throwable.toString());
        }
        return msg.toString();
    }
}
