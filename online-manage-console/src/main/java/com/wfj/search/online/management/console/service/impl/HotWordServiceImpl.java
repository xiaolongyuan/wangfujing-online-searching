package com.wfj.search.online.management.console.service.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.HotWordPojo;
import com.wfj.search.online.common.pojo.HotWordRecordPojo;
import com.wfj.search.online.management.console.mapper.HotWordMapper;
import com.wfj.search.online.management.console.mapper.HotWordRecordMapper;
import com.wfj.search.online.management.console.service.HotWordService;
import com.wfj.search.online.management.console.service.ICacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <br/>create at 15-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("hotWordService")
public class HotWordServiceImpl implements HotWordService {
    private static final Logger logger = LoggerFactory.getLogger(HotWordServiceImpl.class);
    @Autowired
    private HotWordMapper hotWordMapper;
    @Autowired
    private HotWordRecordMapper hotWordRecordMapper;
    @Autowired
    private ICacheService cacheService;

    @Override
    public List<HotWordPojo> list(String site, String channel, int start, int limit) {
        if (start < 0) {
            start = 0;
        }
        if (limit <= 0) {
            return Collections.emptyList();
        }
        return hotWordMapper.list(site, channel, start, limit);
    }

    @Override
    public int count(String site, String channel) {
        return hotWordMapper.count(site, channel);
    }

    @Override
    public HotWordPojo get(String hotWordId) {
        if(StringUtils.isBlank(hotWordId)) {
            return null;
        }
        return hotWordMapper.getBySid(hotWordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(HotWordPojo pojo, String modifier) {
        if (pojo == null || StringUtils.isBlank(pojo.getSite()) || StringUtils.isBlank(pojo.getChannel())
                || StringUtils.isBlank(pojo.getValue()) || StringUtils.isBlank(pojo.getLink())) {
            return -1;
        }
        int row = hotWordMapper.add(pojo);
        addRecord(pojo, modifier, HotWordRecordPojo.ModifyType.ADD);
        return row;
    }

    @Override
    public int mod(HotWordPojo pojo, String modifier) {
        if (pojo == null || StringUtils.isBlank(pojo.getSid()) || StringUtils.isBlank(pojo.getSite())
                || StringUtils.isBlank(pojo.getChannel()) || StringUtils.isBlank(pojo.getValue())
                || StringUtils.isBlank(pojo.getLink())) {
            return -1;
        }
        HotWordPojo old = hotWordMapper.getBySid(pojo.getSid());
        if (old == null) {
            return -2;
        }
        if (old.isEnabled()) {
            return -3;
        }
        int row = hotWordMapper.mod(pojo);
        addRecord(pojo, modifier, HotWordRecordPojo.ModifyType.MOD);
        return row;
    }

    @Override
    public int del(String sid, String modifier) {
        if (StringUtils.isBlank(sid)) {
            return -1;
        }
        HotWordPojo pojo = hotWordMapper.getBySid(sid);
        if (pojo == null) {
            return -2;
        }
        if (pojo.isEnabled()) {
            return -3;
        }
        int row = hotWordMapper.del(pojo);
        addRecord(pojo, modifier, HotWordRecordPojo.ModifyType.DEL);
        return row;
    }

    @Override
    public int enabled(String sid, String modifier) {
        if (StringUtils.isBlank(sid)) {
            return -1;
        }
        HotWordPojo pojo = hotWordMapper.getBySid(sid);
        if (pojo == null) {
            return -2;
        }
        if (pojo.isEnabled()) {
            return 1;
        }
        int row = hotWordMapper.enabled(Lists.newArrayList(sid), true);
        if (row == 1) {
            cacheService.clearHotWords(pojo.getSite(), pojo.getChannel());
            cacheService.clearHotWordsPages();
        }
        addRecord(pojo, modifier, HotWordRecordPojo.ModifyType.ENABLED);
        return row;
    }

    @Override
    public int disabled(String sid, String modifier) {
        if (StringUtils.isBlank(sid)) {
            return -1;
        }
        HotWordPojo pojo = hotWordMapper.getBySid(sid);
        if (pojo == null) {
            return -2;
        }
        if (!pojo.isEnabled()) {
            return 1;
        }
        int row = hotWordMapper.enabled(Lists.newArrayList(sid), false);
        if (row == 1) {
            cacheService.clearHotWords(pojo.getSite(), pojo.getChannel());
            cacheService.clearHotWordsPages();
        }
        addRecord(pojo, modifier, HotWordRecordPojo.ModifyType.DISABLED);
        return row;
    }

    private void addRecord(HotWordPojo pojo, String modifier, HotWordRecordPojo.ModifyType modifyType) {
        try {
            hotWordRecordMapper.add(pojo, modifier, modifyType);
        } catch (Exception e) {
            logger.error("添加热词操作记录失败，热词数据：{}，修改人：{}，修改类型：{}", pojo.toString(), modifier, modifyType.getName(), e);
        }
    }
}
