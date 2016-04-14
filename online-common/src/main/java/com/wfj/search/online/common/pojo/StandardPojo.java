package com.wfj.search.online.common.pojo;

/**
 * 规格POJO
 * <br/>create at 15-7-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class StandardPojo {
    private String standardId;
    private String standardName;

    /**
     * 规格编码
     * @return 规格编码
     */
    public String getStandardId() {
        return standardId;
    }

    /**
     * 规格编码
     * @param standardId 规格编码
     */
    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    /**
     * 规格名称
     * @return 规格名称
     */
    public String getStandardName() {
        return standardName;
    }

    /**
     * 规格名称
     * @param standardName 规格名称
     */
    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }
}
