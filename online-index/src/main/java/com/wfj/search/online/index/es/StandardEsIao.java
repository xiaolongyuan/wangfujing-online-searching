package com.wfj.search.online.index.es;

import com.wfj.search.online.index.pojo.StandardIndexPojo;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface StandardEsIao {
    void upsert(StandardIndexPojo standardIndexPojo);
}
