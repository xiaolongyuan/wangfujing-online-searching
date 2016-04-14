package com.wfj.search.online.web.mapper;

import com.wfj.search.online.web.pojo.HotWordsOfChannelPojo;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test-mapper.xml")
public class HotWordMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(HotWordMapperTest.class);
    @Autowired
    private HotWordMapper hotWordMapper;

    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            SimpleNamingContextBuilder jndiContextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            jndiContextBuilder.bind("mysql/address", "jdbc:mysql://10.6.2.28:3306/wfj-search?characterEncoding=utf-8");
            jndiContextBuilder.bind("mysql/username", "search");
            jndiContextBuilder.bind("mysql/password", "search");
        } catch (Exception e) {
            logger.error("jndi注入错误", e);
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        List<HotWordsOfChannelPojo> hotWords = hotWordMapper.listHotWords(null, null);
        logger.debug(hotWords.toString());
    }
}
