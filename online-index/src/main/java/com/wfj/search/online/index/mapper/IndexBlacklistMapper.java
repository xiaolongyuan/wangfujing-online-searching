package com.wfj.search.online.index.mapper;

import com.wfj.search.online.common.pojo.blacklist.BlacklistPojo;

import java.util.List;

/**
 * 索引黑名单Mapper
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IndexBlacklistMapper {
    public static final String TYPE_ITEM = BlacklistPojo.Type.ITEM.getType();
    public static final String TYPE_SKU = BlacklistPojo.Type.SKU.getType();
    public static final String TYPE_SPU = BlacklistPojo.Type.SPU.getType();
    public static final String TYPE_BRAND = BlacklistPojo.Type.BRAND.getType();

    /**
     * 按类型列出黑名单列表
     * @param type 类型
     * @return 黑名单编码列表
     */
    List<String> listOfType(String type);
}
