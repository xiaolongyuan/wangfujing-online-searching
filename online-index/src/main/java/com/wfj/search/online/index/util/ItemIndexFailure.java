package com.wfj.search.online.index.util;

/**
 * <p>create at 15-10-19</p>
 *
 * @author liufl
 * @since 1.0.0
 * @deprecated 使用 {@link com.wfj.search.online.index.pojo.failure.Failure} 及其子类
 */
@Deprecated
public class ItemIndexFailure extends IndexFailure {
    private String itemCode;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
