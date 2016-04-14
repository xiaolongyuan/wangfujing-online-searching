package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * 无结果建议
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class SuggestionDisplayPojo extends AbstractDisplayPojo {
    private String query;
    private List<SpuDisplayPojo> list = Collections.synchronizedList(Lists.newArrayList());

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<SpuDisplayPojo> getList() {
        return list;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setList(List<SpuDisplayPojo> list) {
        this.list = list;
    }
}
