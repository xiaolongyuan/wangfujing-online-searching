package com.wfj.search.online.statistics.pojo;

import java.sql.Timestamp;

/**
 * <p>create at 15-12-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@SuppressWarnings("UnusedDeclaration")
public class ClickPojo {
    private Long sid;
    private String tid;
    private Long pid;
    private String spu_id;
    private Timestamp click_time;

    public ClickPojo () {}

    public ClickPojo(String tid, Long pid, String spu_id, Timestamp click_time) {
        this.tid = tid;
        this.pid = pid;
        this.spu_id = spu_id;
        this.click_time = click_time;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getSpu_id() {
        return spu_id;
    }

    public void setSpu_id(String spu_id) {
        this.spu_id = spu_id;
    }

    public Timestamp getClick_time() {
        return click_time;
    }

    public void setClick_time(Timestamp click_time) {
        this.click_time = click_time;
    }
}
