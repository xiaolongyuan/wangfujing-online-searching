package com.wfj.search.online.common.pojo.rule;

import java.util.List;

/**
 * 排序规则实体
 * <br/>create at 15-8-4
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SortRulePojoContent {
    private Long sid;// 编号
    private String showText;// 显示文本
    private int showOrder;// 显示顺序
    private String channel;// 渠道

    private boolean selected;
    private List<SortRuleDetailPojo> details;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public int getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(int showOrder) {
        this.showOrder = showOrder;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<SortRuleDetailPojo> getDetails() {
        return details;
    }

    public void setDetails(List<SortRuleDetailPojo> details) {
        this.details = details;
    }
}
