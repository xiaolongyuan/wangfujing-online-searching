package com.wfj.search.online.management.console.mapper;

import com.wfj.search.online.common.pojo.HotWordPojo;
import com.wfj.search.online.common.pojo.HotWordRecordPojo;
import org.apache.ibatis.annotations.Param;

/**
 * <br/>create at 15-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface HotWordRecordMapper {
    /**
     * 添加操作记录
     *
     * @param hotWord    热词对象
     * @param modifier   操作人
     * @param modifyType 操作类型
     * @return 添加数量
     */
    int add(@Param("hotWord") HotWordPojo hotWord, @Param("modifier") String modifier,
            @Param("modifyType") HotWordRecordPojo.ModifyType modifyType);
}
