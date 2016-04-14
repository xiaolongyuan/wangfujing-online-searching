package com.wfj.search.online.web.param;

import com.wfj.search.online.common.pojo.rule.SortRulePojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SortDisplayPojo;
import com.wfj.search.online.web.common.pojo.SortDisplayPojo.ORDER;
import com.wfj.search.online.web.service.ISortService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wfj.search.online.web.common.pojo.SortDisplayPojo.ORDER.ASC;
import static com.wfj.search.online.web.common.pojo.SortDisplayPojo.ORDER.DESC;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("sortParamRestorer")
public class SortParamRestorer implements SearchParamRestorer<String> {
    @Autowired
    private ISortService sortService;

    @Override
    public void restore(SearchParams searchParams, String sortId) {
        SortDisplayPojo sort;
        String channel = searchParams.getChannel();
        if (StringUtils.isBlank(sortId) || "0".equals(sortId.trim())) {
            sort = this.getDefaultSort(channel);
            searchParams.setSort(sort);
            return;
        }
        sort = this.restoreSort(sortId);
        if (sort == null) {
            sort = this.getDefaultSort(channel);
        }
        searchParams.setSort(sort);
    }

    private SortDisplayPojo restoreSort(String sortId) {
        long sid;
        ORDER order;
        try {
            String[] sortId_ = sortId.split("_");
            sid = Long.parseLong(sortId_[0]);
            order = "0".equals(sortId_[1].trim()) ? ASC : DESC;
        } catch (Exception e) {
            throw new IllegalArgumentException("无法按参数恢复排序规则");
        }
        SortRulePojo sortRulePojo = this.sortService.getSortRule(sid);
        SortDisplayPojo pojo = toDisplayPojo(sortRulePojo);
        if (pojo.getOrder().equals(order)) {
            return pojo;
        }
        SortDisplayPojo opposite = pojo.getOpposite();
        pojo.setOpposite(null);
        opposite.setOpposite(pojo);
        if (opposite.getOrder().equals(order)) {
            return opposite;
        }
        return pojo;
    }

    private SortDisplayPojo getDefaultSort(String channel) {
        SortRulePojo sortRulePojo = this.sortService.findDefaultSortRule(channel);
        return toDisplayPojo(sortRulePojo);
    }

    public static SortDisplayPojo toDisplayPojo(SortRulePojo sortRulePojo) {
        try {
            SortDisplayPojo pojo = new SortDisplayPojo();
            if (sortRulePojo.isChannelDefault()) {
                pojo.setDefault(true);
            }
            pojo.setOrder(ORDER.valueOf(sortRulePojo.getDefaultOrderBy().trim().toUpperCase()));
            pojo.setField(sortRulePojo.getOrderField());
            pojo.setDisplay(sortRulePojo.getShowText());
            pojo.setId(sortRulePojo.getSid() + (ASC.equals(pojo.getOrder()) ? "_0" : "_1"));
            ORDER oppositeOrder;
            String otherOrderBy = sortRulePojo.getOtherOrderBy();
            if (StringUtils.isBlank(otherOrderBy)) {
                oppositeOrder = pojo.getOrder();
            } else {
                oppositeOrder = ORDER.valueOf(otherOrderBy.trim().toUpperCase());
            }
            SortDisplayPojo opposite = new SortDisplayPojo();
            opposite.setId(sortRulePojo.getSid() + (ASC.equals(oppositeOrder) ? "_0" : "_1"));
            opposite.setDisplay(pojo.getDisplay());
            opposite.setField(pojo.getField());
            opposite.setOrder(oppositeOrder);
            if (oppositeOrder == pojo.getOrder()) {
                opposite.setDefault(true);
            }
            pojo.setOpposite(opposite);
            return pojo;
        } catch (Exception e) {
            throw new IllegalStateException("搜索排序规则配置有问题！");
        }
    }
}
