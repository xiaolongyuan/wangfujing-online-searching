package com.wfj.search.online.common.pojo.blacklist;

/**
 * <br/>create at 15-10-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public class BlacklistPojo {
    public static final int SUCCESS_DATABASE = 0x01;// 数据库操作成功
    public static final int SUCCESS_INDEX = 0x10;// 索引操作成功
    public static final String[] typeArray = new String[]{
            Type.ITEM.getType(), Type.SKU.getType(), Type.SPU.getType(), Type.BRAND.getType()
    };
    private String id;// 禁用的编码
    private String type;// 禁用的类型 ITEM/SKU/SPU/BRAND
    private String createTime;// 创建时间
    private String creator;// 创建者

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public static enum Type {
        ITEM("ITEM"), SKU("SKU"), SPU("SPU"), BRAND("BRAND");
        private String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}