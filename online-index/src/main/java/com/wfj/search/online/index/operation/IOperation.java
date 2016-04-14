package com.wfj.search.online.index.operation;

import com.wfj.search.util.record.pojo.Operation;

/**
 * 原子操作接口。主要用于进行切面
 * <p>create at 15-7-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IOperation<T> {
    T operate(Operation operation) throws Exception;

    default boolean isRunning() {
        return false;
    }
}
