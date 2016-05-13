package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.common.pojo.Gp;
import com.wfj.search.online.web.es.GpEsIao;
import com.wfj.search.online.web.service.IGpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>create at 16-5-12</p>
 *
 * @author liufl
 * @since 1.0.36
 */
@Service("gpService")
public class GpServiceImpl implements IGpService {
    @Autowired
    private GpEsIao gpEsIao;

    @Override
    public Gp get(String gp) {
        return this.gpEsIao.get(gp);
    }
}
