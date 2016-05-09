package com.wfj.search.online.index.controller.caller.ops;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.index.controller.caller.CallerBase;
import com.wfj.search.utils.zookeeper.discovery.SpringWebMvcServiceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-caller.xml"})
public class FullyRebuildEsControllerCaller extends CallerBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${caller.caller}")
    private String caller;
    @Value("${caller.privateKeyFile}")
    private String privateKeyFile;
    @Autowired
    private SpringWebMvcServiceProvider serviceProvider;

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getCaller() {
        return caller;
    }

    @Override
    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    @Override
    public SpringWebMvcServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    @Test
    public void testFullyRebuildEsManually() {
        String serviceName = "online-fullyRebuildES";
        testTemplate(serviceName, new JSONObject());
    }
}
