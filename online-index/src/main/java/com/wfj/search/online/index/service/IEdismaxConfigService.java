package com.wfj.search.online.index.service;

/**
 * <p>create at 16-2-26</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IEdismaxConfigService {
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
}
