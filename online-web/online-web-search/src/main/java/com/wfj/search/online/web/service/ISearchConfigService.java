package com.wfj.search.online.web.service;

import java.util.Date;
import java.util.List;

/**
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISearchConfigService {
    /**
     * 当前渠道号 WEB渠道号
     *
     * @return 渠道号
     */
    String getChannel();

    /**
     * DisMax查询tie配置
     *
     * @return DisMax查询tie配置
     */
    String getTie();

    /**
     * DisMax查询qf配置
     *
     * @return DisMax查询qf配置
     */
    String getQf();

    /**
     * DisMax查询qs配置
     *
     * @return DisMax查询qs配置
     */
    int getQs();

    /**
     * DisMax查询mm配置
     *
     * @return DisMax查询mm配置
     */
    String getMm();

    /**
     * DisMax查询bq配置
     *
     * @return DisMax查询bq配置
     */
    String getBq();

    /**
     * 每页返回结果数
     *
     * @return 每页返回结果数
     */
    Integer getRows();

    /**
     * WWW主站地址
     *
     * @return WWW主站地址
     */
    String getWwwLocation();

    /**
     * Search站地址
     *
     * @return Search站地址
     */
    String getSearchLocation();

    /**
     * Brand站地址
     *
     * @return Brand站地址
     */
    String getBrandLocation();

    /**
     * List站地址
     *
     * @return List站地址
     */
    String getListLocation();

    /**
     * CSS主机名列表
     *
     * @return CSS主机名列表
     */
    List<String> getCssHosts();

    /**
     * Image主机名列表
     *
     * @return Image主机名列表
     */
    List<String> getImageHosts();

    /**
     * JS主机名列表
     *
     * @return JS主机名列表
     */
    List<String> getJsHosts();
    List<String> getWwwJsHosts();

    /**
     * CSS地址模板
     *
     * @return CSS地址模板
     */
    String getCssLocationTemplate();

    /**
     * Image地址模板
     *
     * @return Image地址模板
     */
    String getImageLocationTemplate();

    /**
     * JS地址模板
     *
     * @return JS地址模板
     */
    String getJsLocationTemplate();
    String getWwwJsLocationTemplate();

    /**
     * 简单查询返回条数
     *
     * @return 简单查询返回条数
     */
    int getSimpleRows();

    int getNumDisplayEntries();

    String getHtmlTbarUri();

    String getHtmlHeaderUri();

    String getHtmlNavigationUri();

    String getHtmlFooterUri();

    String getItemUrlPrefix();

    String getItemUrlPostfix();

    Integer getMayLikeRows();

    /**
     * 默认站点编号
     *
     * @return 默认站点编号
     */
    String getDefaultSite();

    /**
     * 默认频道编号
     *
     * @return 默认频道编号
     */
    String getDefaultChannel();

    /**
     * 新品速递最近时间，单位天
     *
     * @return 最近时间，即距今多少天
     */
    Date getNewProductsDateFrom();
}
