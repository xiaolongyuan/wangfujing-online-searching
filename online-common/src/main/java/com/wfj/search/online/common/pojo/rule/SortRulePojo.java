package com.wfj.search.online.common.pojo.rule;

/**
 * <br/>create at 15-11-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SortRulePojo {
    private String sid;// 排序规则编号
    private String channel;// 渠道
    private String orderField;// 排序字段
    private boolean channelDefault;// 该渠道默认排序
    private String showText;// 显示文本
    private int showOrder;// 显示顺序
    private String defaultOrderBy;// 默认排序规则
    private String otherOrderBy;// 另外一个排序规则(asc或desc)，可以为空，表示只有默认排序

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public boolean isChannelDefault() {
        return channelDefault;
    }

    public void setChannelDefault(boolean channelDefault) {
        this.channelDefault = channelDefault;
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

    public String getDefaultOrderBy() {
        return defaultOrderBy;
    }

    public void setDefaultOrderBy(String defaultOrderBy) {
        this.defaultOrderBy = defaultOrderBy;
    }

    public String getOtherOrderBy() {
        return otherOrderBy;
    }

    public void setOtherOrderBy(String otherOrderBy) {
        this.otherOrderBy = otherOrderBy;
    }
}
