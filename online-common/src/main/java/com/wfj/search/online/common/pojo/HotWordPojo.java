package com.wfj.search.online.common.pojo;

/**
 * <br/>create at 15-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HotWordPojo {
    private String sid;
    private String site;// 所在站点
    private String channel;// 所在频道
    private String value;// 显示内容
    private String link;// 热词链接
    private int orders;// 展示顺序
    private boolean enabled;// 是否有效：true有效，false无效

    public HotWordPojo() {
    }

    public HotWordPojo(String sid, String site, String channel, String value, String link, int orders) {
        this.sid = sid;
        this.site = site;
        this.channel = channel;
        this.value = value;
        this.link = link;
        this.orders = orders;
    }

    public HotWordPojo(String site, String channel, String value, String link, int orders) {
        this.site = site;
        this.channel = channel;
        this.value = value;
        this.link = link;
        this.orders = orders;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "HotWordPojo{" +
                "sid='" + sid + '\'' +
                ", value='" + value + '\'' +
                ", link='" + link + '\'' +
                ", channel='" + channel + '\'' +
                ", site='" + site + '\'' +
                ", orders=" + orders +
                ", enabled=" + enabled +
                '}';
    }
}
