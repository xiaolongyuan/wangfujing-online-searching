package com.wfj.search.online.management.console.mapper.popularize;

import com.wfj.search.online.common.pojo.popularize.CategoryPopularizePositionPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br/>create at 15-8-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface CategoryTopSpotMapper {
    /**
     * 根据条件分页查询坑位信息
     *
     * @param position 坑位条件
     * @param start    起始
     * @param limit    查询条数
     * @return 坑位列表
     */
    List<CategoryPopularizePositionPojo> listWithPage(@Param("position") CategoryPopularizePositionPojo position,
            @Param("start") int start, @Param("limit") int limit);

    /**
     * 根据条件查询坑位条数
     *
     * @param position 坑位条件
     * @return 坑位数量
     */
    long categoryPositionTotal(@Param("position") CategoryPopularizePositionPojo position);

    /**
     * 添加坑位信息
     *
     * @param position 坑位信息
     * @return 添加条数
     */
    int addCategoryPosition(CategoryPopularizePositionPojo position);

    /**
     * 删除坑位信息，修改坑位状态字段status为1
     *
     * @param position 坑位信息
     * @return 删除条数
     */
    int deleteCategoryPosition(CategoryPopularizePositionPojo position);
}
