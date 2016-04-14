package com.wfj.search.online.web.service;

import com.wfj.search.online.web.common.pojo.SearchParams;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISearchParamService {
    /**
     * 根据传入参数值生成 {@link com.wfj.search.online.web.common.pojo.SearchParams} 对象
     *
     * @param inputQuery  检索词
     * @param rows        结果行数
     * @param cat1        一级分类
     * @param cat2        二级分类
     * @param cat3        三级分类
     * @param brandIds    品牌编号，多个编号之间通过下划线（_）间隔
     * @param price       价格区间，上下限之间通过 {@code TO} 间隔。
     * @param standardIds 规格编码列表，多个编号之间通过下划线（_）间隔
     * @param colors      色系编码列表，多个编号之间通过下划线（_）间隔
     * @param attrs       属性列表，多个属性之间通过分号（;）间隔。属性内，属性与属性值之间通过冒号（:）间隔，属性值之间通过下划线（_）间隔
     * @param tagIds      标签编码列表，多个编号之间通过下划线（_）间隔
     * @param sortId      排序方式编码
     * @param currentPage 当前页
     * @param channel     渠道号
     * @return 搜索参数 {@link com.wfj.search.online.web.common.pojo.SearchParams} 对象
     */
    SearchParams restoreParams(String inputQuery, Integer rows, String cat1, String cat2, String cat3, String brandIds,
            String price, String standardIds, String colors, String attrs, String tagIds, String sortId,
            int currentPage, String channel);
}
