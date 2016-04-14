package com.wfj.search.online.index.mapper;

import com.wfj.search.online.common.pojo.boost.ManualBoostPojo;

import java.util.List;

/**
 * <p>create at 15-11-10</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ManualBoostMapper {
    List<ManualBoostPojo> listAll();
}
