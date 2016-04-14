package com.wfj.search.online.common.pojo.interval;

/**
 * 区间明细实体
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public class IntervalDetailPojo {
    private String sid;// 编号
    private String contentSid;// 内容实体编号
    private String upperLimit = "*";// 上限
    private String lowerLimit = "*";// 下限
    private int orderBy;// 排序权重
    private String showText;// 展示值

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getContentSid() {
        return contentSid;
    }

    public void setContentSid(String contentSid) {
        this.contentSid = contentSid;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        if (upperLimit != null && !"".equals(upperLimit.trim())) {
            this.upperLimit = upperLimit.trim();
        }
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        if (lowerLimit != null && !"".equals(lowerLimit.trim())) {
            this.lowerLimit = lowerLimit.trim();
        }
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }
}
