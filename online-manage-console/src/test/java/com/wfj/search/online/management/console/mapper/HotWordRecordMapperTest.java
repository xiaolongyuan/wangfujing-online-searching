package com.wfj.search.online.management.console.mapper;

import com.wfj.search.online.common.pojo.HotWordPojo;
import com.wfj.search.online.common.pojo.HotWordRecordPojo;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test-mapper.xml")
public class HotWordRecordMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(HotWordMapperTest.class);
    @Autowired
    private HotWordRecordMapper hotWordRecordMapper;

    @BeforeClass
    public static void setUp() {
        try {
            SimpleNamingContextBuilder jndiContextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            jndiContextBuilder.bind("mysql/address", "jdbc:mysql://10.6.2.28:3306/wfj-search?characterEncoding=utf-8");
            jndiContextBuilder.bind("mysql/username", "search");
            jndiContextBuilder.bind("mysql/password", "search");
        } catch (Exception e) {
            logger.error("jndi注入错误", e);
        }
    }

    @Test
    public void testAdd() throws Exception {
        HotWordPojo pojo = new HotWordPojo("1", "1", "展示值1", "001", 1);
        pojo.setSid("2");
        hotWordRecordMapper.add(pojo, "not given", HotWordRecordPojo.ModifyType.ADD);
    }
}