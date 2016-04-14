package com.wfj.search.online.management.console.mapper;

import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.HotWordPojo;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test-mapper.xml")
public class HotWordMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(HotWordMapperTest.class);
    @Autowired
    private HotWordMapper hotWordMapper;

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
    @Ignore
    public void testList() throws Exception {
        List<HotWordPojo> list = hotWordMapper.list(null, null, 0, 1);
        assertEquals(1, list.size());
    }

    @Test
    @Ignore
    public void testCount() throws Exception {
        int row = hotWordMapper.count(null, null);
        assertEquals(2, row);
    }

    @Test
    public void testAdd() throws Exception {
        HotWordPojo pojo = new HotWordPojo("1", "1", "展示值", "001", 1);
        int row = hotWordMapper.add(pojo);
        assertEquals(1, row);
    }

    @Test
    @Ignore
    public void testMod() throws Exception {
        HotWordPojo pojo = new HotWordPojo("1", "1", "1", "001", 1);
        pojo.setSid("1");
        pojo.setValue("展示值1");
        int row = hotWordMapper.mod(pojo);
        assertEquals(1, row);
    }

    @Test
    @Ignore
    public void testDel() throws Exception {
        HotWordPojo pojo = new HotWordPojo("1", "1", "展示值1", "001", 1);
        pojo.setSid("1");
        hotWordMapper.enabled(Lists.newArrayList("1"), false);
        int row = hotWordMapper.del(pojo);
        assertEquals(1, row);
    }

    @Test
    @Ignore
    public void testEnabled() throws Exception {
        HotWordPojo pojo = new HotWordPojo("1", "1", "展示值1", "001", 1);
        pojo.setSid("1");
        int row = hotWordMapper.enabled(Lists.newArrayList("1"), true);
        assertEquals(1, row);
    }
}