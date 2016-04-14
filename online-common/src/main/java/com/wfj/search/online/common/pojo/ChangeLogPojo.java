package com.wfj.search.online.common.pojo;

import java.util.Date;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ChangeLogPojo {
    protected String sid;// 序列号
    protected Date modifyTime;// 记录修改时间
    protected String modifier;// 记录修改人
    protected String modifyType;// 操作类型：CREATE/DELETE

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
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

    public String getModifyType() {
        return modifyType;
    }

    public void setModifyType(String modifyType) {
        this.modifyType = modifyType;
    }

    public static enum ModifyType {
        ADD("ADD"),
        DEL("DEL");

        private String type;

        ModifyType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
