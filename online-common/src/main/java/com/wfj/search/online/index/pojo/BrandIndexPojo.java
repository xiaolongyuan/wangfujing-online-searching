package com.wfj.search.online.index.pojo;

import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.beans.Field;

import java.util.Collections;
import java.util.List;

/**
 * 索引用商品品牌POJO
 * <br/>create at 15-7-7
 *
 * @author liufl
 * @since 1.0.0
 */
public class BrandIndexPojo {
    @Field("brandId")
    private String brandId;
    @Field("brandName")
    private String brandName;
    @Field("brandDesc")
    private String brandDesc;
    @Field("brandLogo")
    private String brandLogo;
    @Field("brandPicture")
    private String brandPicture;
    @Field("brandAliases")
    private final List<String> brandAliases = Collections.synchronizedList(Lists.newArrayList());
    @Field("operationSid")
    private Long operationSid;

    /**
     * 品牌编码
     *
     * @return 品牌编码
     */
    public String getBrandId() {
        return brandId;
    }

    /**
     * 品牌编码
     *
     * @param brandId 品牌编码
     */
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    /**
     * 品牌名称
     *
     * @return 品牌名称
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 品牌名称
     *
     * @param brandName 品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 品牌描述
     *
     * @return 品牌描述
     */
    public String getBrandDesc() {
        return brandDesc;
    }

    /**
     * 品牌描述
     *
     * @param brandDesc 品牌描述
     */
    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    /**
     * 品牌LOGO
     *
     * @return 品牌LOGO
     */
    public String getBrandLogo() {
        return brandLogo;
    }

    /**
     * 品牌LOGO
     *
     * @param brandLogo 品牌LOGO
     */
    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    /**
     * 品牌大图
     *
     * @return 品牌大图
     */
    public String getBrandPicture() {
        return brandPicture;
    }

    /**
     * 品牌大图
     *
     * @param brandPicture 品牌大图
     */
    public void setBrandPicture(String brandPicture) {
        this.brandPicture = brandPicture;
    }

    /**
     * 品牌别名
     *
     * @return 品牌别名
     */
    public List<String> getBrandAliases() {
        return brandAliases;
    }

    /**
     * 写入操作记录号
     *
     * @return 写入操作记录号
     */
    public Long getOperationSid() {
        return operationSid;
    }

    /**
     * 写入操作记录号
     *
     * @param operationSid 写入操作记录号
     */
    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }
}
