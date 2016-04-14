package com.wfj.search.online.index.pojo;

/**
 * <p>create at 15-12-3</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class ClickCountPojo {
    private String spuId;
    private int spuClick;

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public int getSpuClick() {
        return spuClick;
    }

    @SuppressWarnings("unused")
    public void setSpuClick(int spuClick) {
        this.spuClick = spuClick;
    }
}
