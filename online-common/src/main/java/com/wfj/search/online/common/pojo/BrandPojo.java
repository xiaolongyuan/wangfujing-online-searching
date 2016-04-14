package com.wfj.search.online.common.pojo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 商品品牌POJO
 * <br/>create at 15-6-29
 *
 * @author liufl
 * @since 1.0.0
 */
public class BrandPojo {
    private String brandId;
    private String brandName;
    private String brandDesc;
    private String brandLogo;
    private String brandPicture;
    private List<String> brandAliases = Lists.newArrayList();

    /**
     * 品牌编码
     * @return 品牌编码
     */
    public String getBrandId() {
        return brandId;
    }

    /**
     * 品牌编码
     * @param brandId 品牌编码
     */
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    /**
     * 品牌名
     * @return 品牌名
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置品牌名
     * @param brandName 品牌名
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 品牌介绍
     * @return 品牌介绍
     */
    public String getBrandDesc() {
        return brandDesc;
    }

    /**
     * 品牌介绍
     * @param brandDesc 品牌介绍
     */
    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    /**
     * 品牌LOGO
     * @return 品牌LOGO
     */
    public String getBrandLogo() {
        return brandLogo;
    }

    /**
     * 品牌LOGO
     * @param brandLogo 品牌LOGO
     */
    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    /**
     * 品牌介绍图片
     * @return 品牌介绍图片
     */
    public String getBrandPicture() {
        return brandPicture;
    }

    /**
     * 品牌介绍图片
     * @param brandPicture 品牌介绍图片
     */
    public void setBrandPicture(String brandPicture) {
        this.brandPicture = brandPicture;
    }

    /**
     * 获取别名列表，包括拼音、翻译
     * @return 别名
     */
    public List<String> getBrandAliases() {
        return brandAliases;
    }

    /**
     * 设置别名列表
     * @param brandAliases 别名，包括拼音、翻译
     */
    public void setBrandAliases(List<String> brandAliases) {
        this.brandAliases = brandAliases;
    }
}
