package com.wfj.search.online.index.util;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * <p>create at 15-11-30</p>
 *
 * @author liufl
 * @since 1.0.0
 * @deprecated 使用 {@link com.wfj.search.online.index.pojo.failure.Failure} 及其子类
 */
@Deprecated
public class IndexFailures {
    private final List<ItemIndexFailure> itemIndexFailures = Collections.synchronizedList(Lists.newArrayList());
    private final List<IndexFailure> otherFailures = Collections.synchronizedList(Lists.newArrayList());

    public List<ItemIndexFailure> getItemIndexFailures() {
        return itemIndexFailures;
    }

    public List<IndexFailure> getOtherFailures() {
        return otherFailures;
    }

    public void addAll(IndexFailures failures) {
        this.itemIndexFailures.addAll(failures.getItemIndexFailures());
        this.otherFailures.addAll(failures.getOtherFailures());
    }
}
