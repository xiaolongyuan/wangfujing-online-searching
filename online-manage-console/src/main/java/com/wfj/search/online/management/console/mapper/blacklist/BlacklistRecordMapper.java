package com.wfj.search.online.management.console.mapper.blacklist;

import com.wfj.search.online.common.pojo.blacklist.BlacklistRecordPojo;

import java.util.List;

/**
 * <br/>create at 15-10-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface BlacklistRecordMapper {
    /**
     * 查询黑名单操作记录
     *
     * @return 黑名单操作记录
     */
    List<BlacklistRecordPojo> listRecord();

    /**
     * 添加记录
     *
     * @param record 记录对象：需要存在字段id、type、modifier、modifyType
     * @return 添加记录数
     */
    int addRecord(BlacklistRecordPojo record);
}
