package com.wfj.search.online.common.pojo;

import java.util.Date;

/**
 * <br/>create at 15-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HotWordRecordPojo {
    protected String sid;// 序列号
    private String hotWordSid;// 热词主键
    private String value;// 显示内容
    private String link;// 热词链接
    private String channel;// 所在频道
    private String site;// 所在站点
    private int orders;// 展示顺序
    private boolean enabled;// 是否有效：true有效，false无效
    protected Date modifyTime;// 记录修改时间
    protected String modifier;// 记录修改人
    protected ModifyType modifyType;// 操作类型：ADD新建/DEL删除/MOD修改/ENABLED使生效/DISABLED使失效

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getHotWordSid() {
        return hotWordSid;
    }

    public void setHotWordSid(String hotWordSid) {
        this.hotWordSid = hotWordSid;
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

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public ModifyType getModifyType() {
        return modifyType;
    }

    public void setModifyType(ModifyType modifyType) {
        this.modifyType = modifyType;
    }

    public static enum ModifyType {
        ADD("ADD"),
        DEL("DEL"),
        MOD("MOD"),
        ENABLED("ENABLED"),
        DISABLED("DISABLED");

        private String name;

        ModifyType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
