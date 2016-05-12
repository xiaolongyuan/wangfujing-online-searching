package com.wfj.search.online.management.console.es;

import com.wfj.search.online.common.pojo.Gp;
import com.wfj.search.online.common.pojo.Page;

/**
 * <p>create at 16-5-11</p>
 *
 * @author liufl
 * @since 1.0.36
 */
public interface GpEsIao {
    Page<Gp> listConfirmed(int start, int fetch);

    void index(Gp gp);

    void confirm(String gp);

    void delete(String gp);
}
