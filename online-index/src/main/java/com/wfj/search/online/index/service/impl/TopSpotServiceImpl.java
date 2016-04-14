package com.wfj.search.online.index.service.impl;

import com.google.common.collect.Maps;
import com.wfj.search.online.index.mapper.BrandTopSpotMapper;
import com.wfj.search.online.index.mapper.CategoryTopSpotMapper;
import com.wfj.search.online.index.pojo.Spot;
import com.wfj.search.online.index.service.ITopSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wfj.search.online.common.constant.IndexCacheableAware.VALUE_KEY_BRAND_TOP_SPOTS;
import static com.wfj.search.online.common.constant.IndexCacheableAware.VALUE_KEY_CATEGORY_TOP_SPOTS;

/**
 * <p>create at 15-12-14</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service
public class TopSpotServiceImpl implements ITopSpotService {
    @Autowired
    private BrandTopSpotMapper brandTopSpotMapper;
    @Autowired
    private CategoryTopSpotMapper categoryTopSpotMapper;

    @Override
    @Cacheable(VALUE_KEY_BRAND_TOP_SPOTS)
    public Map<String, Spot> brandSpots(String brandId, String channel) {
        Map<String, Spot> brandSpots = Maps.newHashMap();
        List<Spot> spotList = this.brandTopSpotMapper.listSpot(brandId, channel);
        for (Spot spot : spotList) {
            brandSpots.put(spot.getSpuId(), spot);
        }
        return brandSpots;
    }

    @Override
    @Cacheable(VALUE_KEY_CATEGORY_TOP_SPOTS)
    public Map<String, Spot> categorySpots(String catId, String channel) {
        Map<String, Spot> categorySpots = Maps.newHashMap();
        List<Spot> spotList = this.categoryTopSpotMapper.listSpot(catId, channel);
        for (Spot spot : spotList) {
            categorySpots.put(spot.getSpuId(), spot);
        }
        return categorySpots;
    }
}
