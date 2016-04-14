package com.wfj.search.online.common.pojo.rule;

import com.wfj.search.online.common.pojo.AbstractDisplayPojo;

/**
 * <br/>create at 15-9-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SortRuleDetailPojo extends AbstractDisplayPojo {
    private Long sid;// 编号
    private String ruleSid;// 排序规则编号
    private String orderFiled;// 排序字段
    private String orderBy;// 排序
    private boolean isDefault;// 是否是该排序规则默认排序

    private boolean selected;
    private SortRuleDetailPojo opposite;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getRuleSid() {
        return ruleSid;
    }

    public void setRuleSid(String ruleSid) {
        this.ruleSid = ruleSid;
    }

    public String getOrderFiled() {
        return orderFiled;
    }

    public void setOrderFiled(String orderFiled) {
        this.orderFiled = orderFiled;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SortRuleDetailPojo getOpposite() {
        return opposite;
    }

    public void setOpposite(SortRuleDetailPojo opposite) {
        this.opposite = opposite;
    }
}
