package com.wfj.search.online.index.pojo.failure;

/**
 * 数据种类：未知、分类、品牌、spu、sku、专柜商品、评论、销量
 * <br/>create at 15-12-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public enum DataType {
    unknown("unknown"),
    category("category"),
    brand("brand"),
    spu("spu"),
    sku("sku"),
    item("item"),
    comment("comment"),
    sale("sale"),
    price("price"),
    suggestion("suggestion");

    private String name;

    DataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
