package com.wfj.search.online.management.console.service.impl;

import com.wfj.search.online.common.pojo.Gp;
import com.wfj.search.online.common.pojo.Page;
import com.wfj.search.online.management.console.es.GpEsIao;
import com.wfj.search.online.management.console.service.GpService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <p>create at 16-5-12</p>
 *
 * @author liufl
 * @since 1.0.36
 */
@Service("gpService")
public class GpServiceImpl implements GpService {
    @Autowired
    private GpEsIao gpEsIao;
    @Autowired
    private CuratorFramework zkClient;
    @Value("${config.gp.sidGenPath}")
    private String gpSidGenPath;
    private DistributedAtomicLong gpSidGen;

    @PostConstruct
    public void afterPropertiesSet() {
        if (this.zkClient.getState() == CuratorFrameworkState.LATENT) {
            this.zkClient.start();
        }
        final ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 5);
        this.gpSidGen = new DistributedAtomicLong(this.zkClient, this.gpSidGenPath, retry);
    }

    @Override
    public Page<Gp> listConfirmed(int start, int fetch) {
        return this.gpEsIao.listConfirmed(start, fetch);
    }

    @Override
    public void createGp(Gp gp) {
        final AtomicValue<Long> increment;
        try {
            increment = this.gpSidGen.increment();
            if (!increment.succeeded()) {
                throw new IllegalStateException("未能通过ZooKeeper生成Gp SID");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final Long sid = increment.postValue();
        gp.setGp(sid.toString());
        this.gpEsIao.index(gp);
    }

    @Override
    public void confirm(String gp) {
        this.gpEsIao.confirm(gp);
    }

    @Override
    public void delete(String gp) {
        this.gpEsIao.delete(gp);
    }
}
