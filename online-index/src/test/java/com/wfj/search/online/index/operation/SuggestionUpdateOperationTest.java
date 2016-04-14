package com.wfj.search.online.index.operation;

import com.wfj.search.online.index.Prepare4Test;
import com.wfj.search.util.record.pojo.Operation;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class SuggestionUpdateOperationTest {
    @Autowired
    @Qualifier("suggestionUpdateOperation")
    private IOperation<Void> suggestionUpdateOperation;
    @Value("${monitor.register.appName}")
    private String appName;
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;

    @BeforeClass
    public static void setUpBeforeClass() {
        Prepare4Test.setUpBeforeClass();
    }

    @Test
    @Ignore
    public void testOperate() throws Throwable {
        Operation opRecord = new Operation();
        opRecord.setAppName(this.appName);
        opRecord.setInstanceName(this.instanceName);
        opRecord.setStartTime(new Timestamp(System.currentTimeMillis()));
        opRecord.setOperation("SUGGESTION_UPDATE");
        opRecord.setParameter("");
        opRecord.setCaller("SYS_INDEX_SERVICE");
        suggestionUpdateOperation.operate(opRecord);
    }
}