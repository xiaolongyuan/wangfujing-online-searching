package com.wfj.search.online.web.test;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.fail;

/**
 * <p>create at 16-1-26</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class LambdaTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static class CheckedOperation {
        public void operate(int v) throws Exception {
            if (v % 3 == 0) {
                throw new Exception("fire!!!");
            }
        }
    }

    @Test
    public void test() {
        List<Integer> integers = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            integers.add(i);
        }
        CheckedOperation operation = new CheckedOperation();
        try {
            integers.forEach((v) -> {
                try {
                    operation.operate(v);
                } catch (Exception e) {
                    throw new RuntimeException(v + " cause operate fail.",e);
                }
            });
            fail("don't caught...");
        } catch (Exception e) {
            logger.info("got it!", e);
        }
    }
}
