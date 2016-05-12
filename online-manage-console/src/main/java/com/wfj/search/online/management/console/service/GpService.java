package com.wfj.search.online.management.console.service;

import com.wfj.search.online.common.pojo.Gp;
import com.wfj.search.online.common.pojo.Page;

/**
 * <p>create at 16-5-12</p>
 *
 * @author liufl
 * @since 1.0.36
 */
public interface GpService {
    Page<Gp> listConfirmed(int start, int fetch);

    void createGp(Gp gp);

    void confirm(String gp);

    void delete(String gp);
}
