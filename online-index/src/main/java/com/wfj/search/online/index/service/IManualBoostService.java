package com.wfj.search.online.index.service;

import java.util.Map;

/**
 * 手工加权服务
 * <p>create at 15-11-10</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IManualBoostService {
    Map<String, Float> listBoosts(String channel);
}
