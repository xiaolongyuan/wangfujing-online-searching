package com.wfj.search.online.statistics.mapper;

import com.wfj.search.online.statistics.pojo.ClickPojo;

/**
 * <p>create at 15-12-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ClickMapper {
    void save(ClickPojo clickPojo);

    int countClickOfSpu(String spu_id);
}
