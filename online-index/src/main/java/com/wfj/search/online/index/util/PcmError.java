package com.wfj.search.online.index.util;

/**
 * <p>create at 15-12-3</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public enum PcmError {
    /**
     * 未上架
     */
    NOT_SELLING_SKU("非上架状态的SKU"),
    /**
     * 未上架
     */
    NOT_SELLING_SPU("非上架状态的SPU"),
//    /**
//     * 获取门店信息失败
//     */
//    NO_SH OP_DATA("获取门店信息失败"),
    /**
     * 获取商品信息失败
     */
    NO_ITEM_DATA("获取商品信息失败"),
    /**
     * 缺少商品颜色主图
     */
    NO_COLOR_MAIN_PIC("缺少商品颜色主图"),
    /**
     * 获取SKU失败
     */
    NO_SKU_DATA("获取对应的SKU信息失败"),
    /**
     * 获取SPU失败
     */
    NO_SPU_DATA("获取对应的SPU信息失败"),
    /**
     * 获取品牌失败
     */
    NO_BRAND_DATA("获取对应的品牌信息失败"),
    /**
     * 获取分类失败
     */
    NO_CATEGORY_DATA("获取所属分类信息过程失败"),
    /**
     * 专柜商品黑名单过滤
     */
    FORBIDDEN_ITEM("被专柜商品黑名单过滤"),
    /**
     * SKU黑名单过滤
     */
    FORBIDDEN_SKU("被SKU黑名单过滤"),
    /**
     * SPU黑名单过滤
     */
    FORBIDDEN_SPU("被SPU黑名单过滤"),
    /**
     * 品牌黑名单过滤
     */
    FORBIDDEN_BRAND("被品牌黑名单过滤"),
    UNKNOWN("未知错误"),
    NO_CHANNEL("没有可售渠道");
    private final String desc;

    PcmError(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
