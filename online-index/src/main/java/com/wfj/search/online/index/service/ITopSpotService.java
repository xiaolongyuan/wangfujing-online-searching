package com.wfj.search.online.index.service;

import com.wfj.search.online.index.pojo.Spot;

import java.util.Map;

/**
 * 置顶坑位
 * <p>create at 15-12-14</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ITopSpotService {
    Map<String, Spot> brandSpots(String brandId, String channel);

    Map<String,Spot> categorySpots(String catId, String channel);
}
