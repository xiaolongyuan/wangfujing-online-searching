package com.wfj.search.online.management.console.service.interval.impl;

import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;
import com.wfj.search.online.management.console.mapper.interval.IntervalContentMapper;
import com.wfj.search.online.management.console.mapper.interval.IntervalDetailMapper;
import com.wfj.search.online.management.console.service.interval.IIntervalContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("intervalContentService")
public class IntervalContentServiceImpl implements IIntervalContentService {
    @Autowired
    private IntervalContentMapper intervalContentMapper;
    @Autowired
    private IntervalDetailMapper intervalDetailMapper;

    @Override
    public List<IntervalContentPojo> intervalContentListWithPage(IntervalContentPojo intervalContent, int start,
            int limit) {
        if (start < 0 || limit <= 0) {
            return Collections.emptyList();
        }
        return intervalContentMapper.intervalContentListWithPage(intervalContent, start, limit);
    }

    @Override
    public long intervalContentTotal(IntervalContentPojo intervalContent) {
        return intervalContentMapper.intervalContentTotal(intervalContent);
    }

    @Override
    public int addIntervalContent(IntervalContentPojo content) {
        if (content == null || content.getChannel() == null || content.getChannel().trim().isEmpty()) {
            return -1;
        }
        content.setSid(null);
        content.setField("currentPrice");
        content.setShowText("价格区间");
        content.setSelected(false);
        return intervalContentMapper.addIntervalContent(content);
    }

    @Override
    public int updateIntervalContent(IntervalContentPojo content) {
        if (content == null
                || content.getSid() == null || content.getSid().trim().isEmpty()
                || content.getChannel() == null || content.getChannel().trim().isEmpty()) {
            return -1;
        }
        IntervalContentPojo c = intervalContentMapper.getIntervalContentBySid(content.getSid());
        if (c == null) {
            return 0;
        } else if (c.isSelected()) {
            return -2;
        }
        content.setField("currentPrice");
        content.setShowText("价格区间");
        content.setSelected(false);
        return intervalContentMapper.updateIntervalContent(content);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteIntervalContent(String sid) {
        if (StringUtils.isBlank(sid)) {
            return -1;
        }
        IntervalContentPojo c = intervalContentMapper.getIntervalContentBySid(sid);
        if (c == null) {
            return 0;
        } else if (c.isSelected()) {
            return -2;
        }
        int row = intervalContentMapper.deleteIntervalContent(sid);
        intervalDetailMapper.deleteIntervalDetailByContentSid(sid);
        return row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public int doSelectedInterval(String sid) {
        List<IntervalDetailPojo> detailList = intervalDetailMapper.listDetailByContentSid(sid);
        if (detailList.isEmpty()) {
            return -1;
        }
        intervalContentMapper.unselected(sid);
        int row = intervalContentMapper.doSelected(sid);
        if (row == 0) {
            throw new IllegalArgumentException("请求有效的价格区间编号不存在于数据库，请求失败。");
        }
        this.clearCache();
        return row;
    }
}
