package com.wfj.search.online.index.service.impl;

import com.google.common.collect.Sets;
import com.wfj.search.online.index.mapper.IndexBlacklistMapper;
import com.wfj.search.online.index.service.IIndexBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_INDEX_BLACKLIST;

/**
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("indexBlacklistService")
public class IndexBlacklistServiceImpl implements IIndexBlacklistService {
    @Autowired
    private IndexBlacklistMapper indexBlacklistMapper;

    @Override
    @Cacheable(value = VALUE_KEY_INDEX_BLACKLIST)
    public Set<String> listOfType(String type) {
        List<String> list = indexBlacklistMapper.listOfType(type);
        return list == null ? Sets.newHashSet() : Sets.newHashSet(list);
    }
}
