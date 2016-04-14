package com.wfj.search.online.web.test;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * 测试两种请求solr的层面分析方式：1、所有层面分析在一个请求；2、不同层面分析在不同请求。
 * <br/>create at 15-12-16
 *
 * @author liuxh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test-solr.xml")
@Ignore
public class TestSolrQuery {
    private static final Logger logger = LoggerFactory.getLogger(TestSolrQuery.class);
    @Autowired
    private SolrClient solrClient;
    private static CompletionService<Void> completionService;
    private static int count = 100;

    @BeforeClass
    public static void beforeClass() {
        int threadCount = 20 * Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(threadCount, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("testThread");
            return thread;
        });
        completionService = new ExecutorCompletionService<>(pool);
    }

    @Test
    public void test() throws Exception {
        // count = 100, 8767 8633
        // count = 1000, 8882 8801
        // count = 10000, 27366 27500
        long begin = System.currentTimeMillis();
        CountDownLatch switchCount = new CountDownLatch(1);
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            completionService.submit(() -> {
                try {
                    switchCount.await();
                } catch (InterruptedException e) {
                    logger.error("线程被打断", e);
                }
                try {
                    testOneTime();
                } catch (Exception e) {
                    logger.error("查询solr出错", e);
                }
            }, null);
            countDownLatch.countDown();
        }
        switchCount.countDown();
        countDownLatch.await();
        for (int i = 0; i < count; i++) {
            completionService.take();
        }
        long end = System.currentTimeMillis();
        logger.info((end - begin) + "");
    }

    @Test
    public void test2() throws Exception {
        // count = 100, 8470 8710
        // count = 1000, 8403 8772
        // count = 10000, 56814 57101
        long begin = System.currentTimeMillis();
        CountDownLatch switchCount = new CountDownLatch(1);
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            completionService.submit(() -> {
                try {
                    switchCount.await();
                } catch (InterruptedException e) {
                    logger.error("线程被打断", e);
                }
                try {
                    testMultiTimes();
                } catch (Exception e) {
                    logger.error("查询solr出错", e);
                }
            }, null);
            countDownLatch.countDown();
        }
        switchCount.countDown();
        countDownLatch.await();
        for (int i = 0; i < count; i++) {
            completionService.take();
        }
        long end = System.currentTimeMillis();
        logger.info((end - begin) + "");
    }

    @Test
    public void testOneTime() throws Exception {
        SolrQuery query = new SolrQuery();
        query
                .setFacet(true)
                .setFacetMinCount(1)
                .setRows(0)
                .setFacet(true)
                .setFacetMinCount(1)
                .setFacetLimit(Integer.MAX_VALUE);
        query
                .addFacetField("brandCode")
                .addFacetField("shoppeCode")
                .addFacetField("storeCode");

        QueryResponse response = solrClient.query("shoppe-item", query);

        FacetField brandCodeFF = response.getFacetField("brandCode");
        List<FacetField.Count> brandCodeCounts = brandCodeFF.getValues();
        /* 删除门店内容，故注释门店编号层面分析
        FacetField shoppeCodeFF = response.getFacetField("shoppeCode");
        List<FacetField.Count> shoppeCodeCounts = shoppeCodeFF.getValues();
        */
        FacetField storeCodeFF = response.getFacetField("storeCode");
        List<FacetField.Count> storeCodeCounts = storeCodeFF.getValues();
    }

    @Test
    public void testMultiTimes() throws Exception {
        ExecutorService _pool = Executors.newFixedThreadPool(3, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("testMultiTimesThread");
            return thread;
        });
        CompletionService<Void> _completionService = new ExecutorCompletionService<>(_pool);
        SolrQuery query = new SolrQuery();
        query
                .setFacet(true)
                .setFacetMinCount(1)
                .setRows(0)
                .setFacet(true)
                .setFacetMinCount(1)
                .setFacetLimit(Integer.MAX_VALUE);
        try {
            _completionService.submit(() -> {
                try {
                    SolrQuery brandCodeQuery = query.getCopy().addFacetField("brandCode");
                    QueryResponse brandCodeResponse = solrClient.query("shoppe-item", brandCodeQuery);
                    FacetField brandCodeFF = brandCodeResponse.getFacetField("brandCode");
                    List<FacetField.Count> brandCodeCounts = brandCodeFF.getValues();
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();
                }
            }, null);
            /* 删除门店内容，故注释门店编号层面分析
            _completionService.submit(() -> {
                try {
                    SolrQuery shoppeCodeQuery = query.getCopy().addFacetField("shoppeCode");
                    QueryResponse shoppeCodeResponse = solrClient.query("shoppe-item", shoppeCodeQuery);
                    FacetField shoppeCodeFF = shoppeCodeResponse.getFacetField("shoppeCode");
                    List<FacetField.Count> shoppeCodeCounts = shoppeCodeFF.getValues();
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();
                }
            }, null);
            */
            _completionService.submit(() -> {
                try {
                    SolrQuery storeCodeQuery = query.getCopy().addFacetField("storeCode");
                    QueryResponse storeCodeResponse = solrClient.query("shoppe-item", storeCodeQuery);
                    FacetField storeCodeFF = storeCodeResponse.getFacetField("storeCode");
                    List<FacetField.Count> storeCodeCounts = storeCodeFF.getValues();
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();
                }
            }, null);
            for (int i = 0; i < 3; i++) {
                _completionService.take();
            }
        } finally {
            _pool.shutdown();
        }
    }
}
