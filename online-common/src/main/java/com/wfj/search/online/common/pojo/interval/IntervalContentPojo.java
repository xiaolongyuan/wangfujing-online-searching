package com.wfj.search.online.common.pojo.interval;

/**
 * 区间内容实体
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public class IntervalContentPojo {
    private String sid;// 编号
    private String field;// 字段
    private String showText;// 显示内容
    private String channel;// 渠道
    private boolean selected;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
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

    @Override
    public String toString() {
        return "IntervalContentPojo_" + field;
    }
}
