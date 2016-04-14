package com.wfj.search.online.management.console.service.boost;

import com.wfj.search.online.common.pojo.boost.ManualBoostPojo;

import java.util.List;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IManualBoostService {
    List<ManualBoostPojo> list(String skuId, int start, int limit);

    int count(String skuId);

    int save(String skuId, float boost);

    int delete(String skuId);
}
