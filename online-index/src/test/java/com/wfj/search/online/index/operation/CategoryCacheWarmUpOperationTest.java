package com.wfj.search.online.index.operation;

import com.wfj.platform.util.analysis.Timer;
import com.wfj.search.util.record.pojo.Operation;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.time.Duration;

/**
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class CategoryCacheWarmUpOperationTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("categoryCacheWarmUpOperation")
    private IOperation<Void> categoryCacheWarmUpOperation;
    @Value("${monitor.register.appName}")
    private String appName;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        SimpleNamingContextBuilder jndiContextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        jndiContextBuilder.bind("service/address", "127.0.0.1:8080");
        jndiContextBuilder.bind("service/instance", "indexServiceTest");
    }

    @Test
    public void testOperate() throws Throwable {
        Operation opRecord = new Operation();
        opRecord.setAppName(this.appName);
        opRecord.setInstanceName(this.instanceName);
        opRecord.setStartTime(new Timestamp(System.currentTimeMillis()));
        opRecord.setOperation("CATEGORY_WARM_UP");
        opRecord.setParameter("");
        opRecord.setCaller("SYS_INDEX_SERVICE");
        Timer timer = new Timer();
        timer.start();
        categoryCacheWarmUpOperation.operate(opRecord);
        Duration stop = timer.stop();
        logger.info("category warm up costs: {}", stop.toString());
    }
}
