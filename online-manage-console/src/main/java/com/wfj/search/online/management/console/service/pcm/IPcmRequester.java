package com.wfj.search.online.management.console.service.pcm;

import com.alibaba.fastjson.JSONArray;
import com.wfj.search.online.common.pojo.CategoryPojo;

import java.util.List;
import java.util.Optional;

/**
 * <br/>create at 15-8-21
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public interface IPcmRequester {
    /**
     * 返回产品品牌列表。
     *
     * @return 所有产品品牌信息
     */
    JSONArray listBrands();

    /**
     * 根据渠道号查询分类树列表
     *
     * @param channel 渠道号
     * @return 分类树列表
     */
    List<CategoryPojo> listCategoryByChannel(String channel);

    /**
     * 根据父级节点编号获取所属子节点列表树
     *
     * @param parentId 父级节点编号，如果为null，返货所有子级节点树。
     * @return 子级节点列表树
     */
    List<CategoryPojo> listDisplayCategoryChildren(String parentId);

    /**
     * 根据分类编号获取分类数据
     *
     * @param categoryId 分类编号
     * @return 分类数据
     */
    Optional<CategoryPojo> getCategoryInfo(String categoryId);

    /**
     * 列出渠道列表
     *
     * @return 渠道列表
     */
    JSONArray listChannels();
}
