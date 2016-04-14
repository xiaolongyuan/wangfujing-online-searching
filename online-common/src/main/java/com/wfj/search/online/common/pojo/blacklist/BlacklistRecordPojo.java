package com.wfj.search.online.common.pojo.blacklist;

import com.wfj.search.online.common.pojo.ChangeLogPojo;

/**
 * <br/>create at 15-10-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public class BlacklistRecordPojo extends ChangeLogPojo {
    private String id;// 禁用的编码
    private String type;// 禁用的类型 ITEM/SKU/SPU/BRAND

    public BlacklistRecordPojo() {
    }

    public BlacklistRecordPojo(String id, String type, String modifier, ModifyType modifyType) {
        this.id = id;
        this.type = type;
        this.modifier = modifier;
        this.modifyType = modifyType.getType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
