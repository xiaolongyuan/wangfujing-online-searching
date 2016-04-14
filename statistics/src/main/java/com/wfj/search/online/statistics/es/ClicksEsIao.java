package com.wfj.search.online.statistics.es;

import com.wfj.search.online.statistics.pojo.ClickCountPojo;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface ClicksEsIao {
    void upsert(ClickCountPojo clickCountPojo);
}
