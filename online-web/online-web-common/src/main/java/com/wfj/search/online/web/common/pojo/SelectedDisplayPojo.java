package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * 已选项
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class SelectedDisplayPojo<T> extends AbstractDisplayPojo {
    private final List<T> selected = Collections.synchronizedList(Lists.newArrayList());

    public List<T> getSelected() {
        return selected;
    }
}
