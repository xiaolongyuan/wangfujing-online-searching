package com.wfj.search.online.index.util;

/**
 * 索引统计类型
 * <br/>create at 15-7-26
 *
 * @author liufl
 * @since 1.0.0
 */
public enum IndexResultType {
    /**
     * 所有报告的记录
     */
    ALL("报告的记录总数"),
    /**
     * 未上架
     */
    NOT_SELLING_SKU("非上架状态的SKU"),
    /**
     * 未上架
     */
    NOT_SELLING_SPU("非上架状态的SPU"),
    /**
     * 正常写入索引
     */
    OK("成功索引"),
//    /**
//     * 获取门店信息失败
//     */
//    NO_S HOP_DATA("获取门店信息失败"),
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
     * 运行时异常,写入失败
     */
    EXCEPTION("将组装的数组写入索引时失败"),
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
    /**
     * 当前统计中相对报告数缺少的数量
     */
    LOST("最终访问记录数比PCM报告的记录数缺少");

    private final String desc;

    IndexResultType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
