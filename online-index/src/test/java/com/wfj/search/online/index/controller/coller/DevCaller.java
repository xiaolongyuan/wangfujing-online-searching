package com.wfj.search.online.index.controller.coller;

import com.wfj.platform.util.httpclient.HttpRequester;
import com.wfj.platform.util.signature.json.JsonSigner;
import com.wfj.platform.util.signature.keytool.KeyUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * <p>create at 15-12-17</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Ignore
public class DevCaller extends CallerBase {
    private static final String dbUrl = "jdbc:mysql://10.6.2.28:3306/wfj-search?characterEncoding=utf-8";
    private static final String dbUsername = "search";
    private static final String dbPassword = "search";
    Logger logger = LoggerFactory.getLogger(getClass());
    private Connection connection = null;
    private String caller;
    private String noParamsMessage;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Override
    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        }
        return connection;
    }

    @Override
    protected String getCaller() {
        if (this.caller == null) {
            this.caller = "SEARCH_TESTER-" + UUID.randomUUID().toString().substring(20);
        }
        return this.caller;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        logger.debug("public key:\n{}", KeyUtils.toBase64String(this.keyPair.getPublic()));

        PrivateKey privateKey = this.keyPair.getPrivate();
        JSONObject messageBody = new JSONObject();
        noParamsMessage = JsonSigner.wrapSignature(messageBody, privateKey, this.getCaller(), "admin");
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void callRemoveItem() {
        String url = "http://127.0.0.1:9080/online-index/ops/item/removeItem.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("itemId", "40000660");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callIndexItem() {
        String url = "http://127.0.0.1:9080/online-index/ops/item/indexItem.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("itemId", "40000660");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callRemoveBrandItems() {
        String url = "http://127.0.0.1:9080/online-index/ops/brand/removeItems.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("brandId", "1000103");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callIndexBrandItems() {
        String url = "http://127.0.0.1:9080/online-index/ops/brand/indexItems.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("brandId", "1000103");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callIndexCategoryItems() {
        String url = "http://127.0.0.1:9080/online-index/ops/category/indexItems.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("categoryId", "654");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callRemoveSkuItems() {
        String url = "http://127.0.0.1:9080/online-index/ops/sku/removeItems.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("skuId", "2000000001158");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callIndexSkuItems() {
        String url = "http://127.0.0.1:9080/online-index/ops/sku/indexItems.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("skuId", "2000000001158");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callRemoveSpuItems() {
        String url = "http://127.0.0.1:9080/online-index/ops/spu/removeItems.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("spuId", "200001209");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callIndexSpuItems() {
        String url = "http://127.0.0.1:9080/online-index/ops/spu/indexItems.json";
        JSONObject messageBody = new JSONObject();
        messageBody.put("spuId", "200001209");
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
            logger.info(s);
            JSONObject res = JSONObject.fromObject(s);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callFullyRebuildComment(){
        String url = "http://127.0.0.1:9080/online-index/ops/comment/fullyRebuildComment.json";
        JSONArray messageBody = new JSONArray();
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        logger.debug(message);
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, message);
            assertNotNull(s);
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }

    @Test
    public void callIndexComments() {
        String url = "http://127.0.0.1:9080/online-index/mq/comment/indexComments.json";
        JSONArray messageBody = new JSONArray();
        JSONObject comment = new JSONObject();
        comment.put("sid", "1");
        comment.put("memberId", "2");
        comment.put("spuId", "3");
        comment.put("skuId", "4");
        comment.put("itemId", "5");
        comment.put("title", "");
        comment.put("content", "测试评论内容");
        comment.put("score", "5");
        comment.put("createTime", "2015-12-25 09:14:20");
        comment.put("orderNo", "test0001");
        comment.put("productName", "测试商品");
        comment.put("orderCreatedTime", "2015-12-25 19:14:20");
        comment.put("isAnonymity", "1");
        comment.put("mind", "1,2,3,4");
        JSONArray pics = new JSONArray();
        pics.add("test1/test1");
        pics.add("test1/test2");
        pics.add("test2/test1");
        comment.put("pics", pics);
        messageBody.add(comment);
        String message = JsonSigner.wrapSignature(messageBody, this.keyPair.getPrivate(), this.getCaller(), "admin");
        JSONObject mqMessage = new JSONObject();
        mqMessage.put("header", "{\"version\": \"1\"," +
                "\"sourceSysID\": \"P012\"," +
                "\"serviceID\": \"P202_09\"," +
                "\"priority\": \"1\"," +
                "\"count\": \"1\"," +
                "\"bizType\": \"17\"," +
                "\"token\": null," +
                "\"createTime\": null," +
                "\"field1\": null," +
                "\"field2\": null," +
                "\"field3\": null," +
                "\"messageID\": \"aaf05da9-5d5b-4d94-8dc7-41b767c679b0\"," +
                "\"messageType\": null," +
                "\"routeKey\": \"2\"," +
                "\"destUrl\": \"http://10.6.2.55:7080/shoppe-index/message-actuated/indexErpProducts.json\"," +
                "\"callbackUrl\": \"http\"," +
                "\"destCallType\": 0}");
        mqMessage.put("data", message);
        logger.debug(mqMessage.toString());
        try {
            String s = HttpRequester.getSimpleHttpRequester().httpPostString(url, mqMessage);
            assertNotNull(s);
        } catch (Exception e) {
            logger.error("调用错误", e);
            fail(e.getMessage());
        }
    }
}
