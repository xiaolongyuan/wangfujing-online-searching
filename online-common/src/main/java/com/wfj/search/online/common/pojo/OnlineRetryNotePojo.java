package com.wfj.search.online.common.pojo;

import java.util.Date;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public class OnlineRetryNotePojo {
    private String sid;
    private String code;// 专柜商品编码
    private boolean resolved;// 失败创建是否解决
    private Step step = Step.unknown;// 失败位置：request、es、index、success
    private Type type = Type.unknown;// 重试类别：category、brand、spu、sku、item、comment
    private Action action = Action.unknown;// 失败操作的类型：create、delete
    private long version;// 乐观锁标记
    private Date updateTime;// 该记录修改时间
    private String comment;// 备注信息

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @SuppressWarnings("unused")
    public boolean isResolved() {
        return resolved;
    }

    @SuppressWarnings("unused")
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @SuppressWarnings("unused")
    public Step getStep() {
        return step;
    }

    @SuppressWarnings("unused")
    public void setStep(Step step) {
        this.step = step;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @SuppressWarnings("unused")
    public Date getUpdateTime() {
        return updateTime;
    }

    @SuppressWarnings("unused")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 失败位置：request、es、index、success
     */
    public static enum Step {
        unknown("unknown"),
        request("request"),
        es("es"),
        index("index"),
        success("success");

        private String name;

        Step(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 重试类别：category、brand、spu、sku、item、comment
     */
    public static enum Type {
        unknown("unknown"),
        category("category"),
        brand("brand"),
        spu("spu"),
        sku("sku"),
        item("item"),
        comment("comment");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 失败的操作：create、delete
     */
    public static enum Action {
        unknown("unknown"),
        save("save"),
        delete("delete");

        private String name;

        Action(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
