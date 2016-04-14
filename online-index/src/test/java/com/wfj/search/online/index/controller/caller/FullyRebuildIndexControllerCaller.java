package com.wfj.search.online.index.controller.caller;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.SpringMvcServiceProvider;
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
public class FullyRebuildIndexControllerCaller extends CallerBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${caller.caller}")
    private String caller;
    @Value("${caller.privateKeyFile}")
    private String privateKeyFile;
    @Autowired
    private SpringMvcServiceProvider serviceProvider;

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
    public SpringMvcServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    @Test
    public void testFullyRebuildIndexManuallyFromEs() {
        String serviceName = "online-fullyRebuildIndexFromEs";
        testTemplate(serviceName, new JSONObject());
    }
}
