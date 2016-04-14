package com.wfj.search.online.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * <br/>create at 15-7-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class Prepare4Test {
    private final static Logger logger = LoggerFactory.getLogger(Prepare4Test.class);

    public static void setUpBeforeClass() {
        try {
            SimpleNamingContextBuilder jndiContextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            jndiContextBuilder.bind("mysql/address", "jdbc:mysql://10.6.2.28:3306/wfj-search?characterEncoding=utf-8");
            jndiContextBuilder.bind("mysql/username", "search");
            jndiContextBuilder.bind("mysql/password", "search");
            jndiContextBuilder.bind("service/address", "127.0.0.1:8080");
            jndiContextBuilder.bind("service/instance", "indexServiceTest");
        } catch (Exception e) {
            logger.error("jndi注入错误", e);
        }
    }
}
