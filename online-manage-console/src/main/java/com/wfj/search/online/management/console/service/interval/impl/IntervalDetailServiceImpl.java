package com.wfj.search.online.management.console.service.interval.impl;

import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;
import com.wfj.search.online.management.console.mapper.interval.IntervalContentMapper;
import com.wfj.search.online.management.console.mapper.interval.IntervalDetailMapper;
import com.wfj.search.online.management.console.service.interval.IIntervalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("intervalDetailService")
public class IntervalDetailServiceImpl implements IIntervalDetailService {
    @Autowired
    private IntervalDetailMapper intervalDetailMapper;
    @Autowired
    private IntervalContentMapper intervalContentMapper;

    @Override
    public List<IntervalDetailPojo> intervalDetailListWithPage(IntervalDetailPojo intervalDetail, int start,
            int limit) {
        if (intervalDetail == null
                || intervalDetail.getContentSid() == null || intervalDetail.getContentSid().isEmpty()
                || intervalDetail.getContentSid().equals("null")
                || start < 0 || limit <= 0) {
            return Collections.emptyList();
        }
        IntervalContentPojo content = intervalContentMapper.getIntervalContentBySid(intervalDetail.getContentSid());
        if (content == null) {
            return Collections.emptyList();
        }
        return intervalDetailMapper.intervalDetailListWithPage(intervalDetail, start, limit);
    }

    @Override
    public long intervalDetailTotal(IntervalDetailPojo intervalDetail) {
        if (intervalDetail == null || intervalDetail.getContentSid() == null || intervalDetail.getContentSid()
                .isEmpty()) {
            return 0;
        }
        IntervalContentPojo content = intervalContentMapper.getIntervalContentBySid(intervalDetail.getContentSid());
        if (content == null) {
            return 0;
        }
        return intervalDetailMapper.intervalDetailTotal(intervalDetail);
    }

    @Override
    public int createIntervalDetail(IntervalDetailPojo detail) {
        if (dataError(detail)) {
            return -1;
        }
        IntervalContentPojo content = new IntervalContentPojo();
        content.setSid(detail.getContentSid());
        List<IntervalContentPojo> list = intervalContentMapper.getIntervalContentList(content);
        if (list.size() == 0) {
            return 0;
        }
        return intervalDetailMapper.addIntervalDetail(completeLimit(detail));
    }

    @Override
    public int updateIntervalDetail(IntervalDetailPojo detail) {
        if (dataError(detail) || detail.getSid() == null || detail.getSid().trim().isEmpty()) {
            return -1;
        }
        IntervalContentPojo content = new IntervalContentPojo();
        content.setSid(detail.getContentSid());
        List<IntervalContentPojo> list = intervalContentMapper.getIntervalContentList(content);
        if (list.size() == 0) {
            return 0;
        }
        return intervalDetailMapper.updateIntervalDetail(completeLimit(detail));
    }

    private boolean dataError(IntervalDetailPojo detail) {
        if (detail == null || detail.getContentSid() == null || detail.getContentSid().trim().isEmpty()
                || detail.getLowerLimit() == null || detail.getLowerLimit().trim().isEmpty()
                || detail.getUpperLimit() == null || detail.getUpperLimit().trim().isEmpty()) {
            return true;
        }
        if ("*".equals(detail.getLowerLimit())) {
            // 下限是*，则上限是*或数字，数据都没有错误，即返回false。
            return !("*".equals(detail.getUpperLimit()) || isNumber(detail.getUpperLimit()));
        }
        if (isNumber(detail.getLowerLimit())) {
            // 下限是数字，上限是*或上限是数字且大于下限，数据没有错误，即返回false。
            return !("*".equals(detail.getUpperLimit())
                    || (isNumber(detail.getUpperLimit())
                    && Double.valueOf(detail.getLowerLimit()) < Double.valueOf(detail.getUpperLimit())));
        }
        return true;
    }

    private boolean isNumber(String num) {
        Pattern p = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        return p.matcher(num).matches();
    }

    private IntervalDetailPojo completeLimit(IntervalDetailPojo detail) {
        if (detail == null) {
            return null;
        }
        if (detail.getLowerLimit() == null || detail.getLowerLimit().trim().isEmpty()) {
            detail.setLowerLimit("*");
        }
        if (detail.getUpperLimit() == null || detail.getUpperLimit().trim().isEmpty()) {
            detail.setUpperLimit("*");
        }
        return detail;
    }

    @Override
    public int deleteIntervalDetail(IntervalDetailPojo detail) {
        if (detail == null || detail.getSid() == null || detail.getSid().trim().isEmpty()) {
            return -1;
        }
        return intervalDetailMapper.deleteIntervalDetail(detail.getSid());
    }
}
