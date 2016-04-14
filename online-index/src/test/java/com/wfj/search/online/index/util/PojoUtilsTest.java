package com.wfj.search.online.index.util;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.json.JsonGetPartException;
import com.wfj.search.online.common.pojo.BrandPojo;
import com.wfj.search.online.common.pojo.ItemPojo;
import com.wfj.search.online.common.pojo.SkuPojo;
import com.wfj.search.online.common.pojo.SpuPojo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PojoUtilsTest {
    @Test
    public void testJson2Item() throws Exception {
        JSONObject jsonObject = JSONObject.parseObject("{\n" +
                "    \"itemId\":\"1\",\n" +
                "    \"skuId\":\"1\",\n" +
                "    \"supplierId\":\"1\",\n" +
                "    \"onSell\":true,\n" +
                "    \"inventory\":10,\n" +
                "    \"originalPrice\":1000,\n" +
                "    \"currentPrice\":200\n" +
                "  }\n");
        ItemPojo item = PojoUtils.json2Item(jsonObject);
        assertNotNull(item);
        assertEquals(item.getOriginalPrice().toString(), "1000");
    }

    @Test
    public void testJson2Sku() throws Exception {
        JSONObject jsonObject = JSONObject.parseObject("{\n" +
                "    \"skuId\":\"1\",\n" +
                "    \"spuId\":\"1\",\n" +
                "    \"colorId\":\"1\",\n" +
                "    \"colorName\":\"RED\",\n" +
                "    \"colorAlias\":\"红\",\n" +
                "    \"standardId\":\"1\",\n" +
                "    \"standardName\":\"L\",\n" +
                "    \"pictures\":[\n" +
                "      {\n" +
                "        \"pictureSid\":\"1\",\n" +
                "        \"picture\":\"http://host/path/pic.png\",\n" +
                "        \"colorId\":\"1\",\n" +
                "        \"order\":1,\n" +
                "        \"colorAlias\":\"红\",\n" +
                "        \"colorMaster\":true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n");
        SkuPojo pojo = PojoUtils.json2Sku(jsonObject);
        assertNotNull(pojo);
        assertEquals(pojo.getSkuId(), "1");
        assertEquals(pojo.getStandardPojo().getStandardName(), "L");
        assertEquals(pojo.getPictures().size(), 1);
        assertEquals(pojo.getPictures().get(0).getPicture(), "http://host/path/pic.png");
    }

    @Test
    public void testJson2Spu() throws Exception {
        JSONObject jsonObject = JSONObject.parseObject("{\n" +
                "    \"spuId\": \"1\",\n" +
                "    \"spuName\": \"ABC123\",\n" +
                "    \"model\": \"1A2B3C\",\n" +
                "    \"brandId\": \"1\",\n" +
                "    \"activeBit\": \"1\",\n" +
                "    \"onSell\": true,\n" +
                "    \"pageDescription\": \"......\",\n" +
                "    \"aliases\": [\n" +
                "        \"PA-A\",\n" +
                "        \"PA_B\"\n" +
                "    ],\n" +
                "    \"onSellSince\": \"2015-06-30 00:00:59\",\n" +
                "    \"categoryIds\": [\n" +
                "        \"1\",\n" +
                "        \"2\"\n" +
                "    ],\n" +
                "    \"tags\": [\n" +
                "        {\n" +
                "            \"tagId\": \"1\",\n" +
                "            \"tag\": \"TAG1\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"propertyValues\": [\n" +
                "        {\n" +
                "            \"propertyId\": \"1\",\n" +
                "            \"propertyName\": \"领型\",\n" +
                "            \"propertyOrder\": 1,\n" +
                "            \"enumProperty\": true,\n" +
                "            \"propertyValueId\": \"1\",\n" +
                "            \"propertyValue\": \"立领\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"propertyId\": \"2\",\n" +
                "            \"propertyName\": \"充绒量\",\n" +
                "            \"propertyOrder\": 2,\n" +
                "            \"enumProperty\": false,\n" +
                "            \"propertyValue\": \"91g\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"masterPicture\": {\n" +
                "        \"pictureSid\": \"1\",\n" +
                "        \"picture\": \"http://host/path/pic.png\",\n" +
                "        \"colorId\": \"1\",\n" +
                "        \"colorAlias\": \"红\"\n" +
                "    }\n" +
                "}");
        SpuPojo pojo = PojoUtils.json2Spu(jsonObject);
        assertNotNull(pojo);
        assertEquals(pojo.getAliases().size(), 2);
        assertEquals(pojo.getCategoryIds().size(), 2);
        assertEquals(pojo.getTags().size(), 1);
        assertEquals(pojo.getTags().get(0).getTag(), "TAG1");
        assertEquals(pojo.getPropertyValues().size(), 2);
        assertEquals(pojo.getPropertyValues().get(0).getProperty().getPropertyName(), "领型");
        assertEquals(pojo.getPropertyValues().get(0).getPropertyValue(), "立领");
        assertEquals(pojo.getPropertyValues().get(1).getProperty().getPropertyName(), "充绒量");
        assertEquals(pojo.getPropertyValues().get(1).getPropertyValue(), "91g");
    }

    @Test
    public void json2Brand() throws JsonGetPartException {
        JSONObject json = JSONObject.parseObject("{\n" +
                "    \"brandId\":\"1\",\n" +
                "    \"brandName\":\"brand1\",\n" +
                "    \"brandDesc\":\"brand description\",\n" +
                "    \"brandLogo\":\"logo.png\",\n" +
                "    \"brandPicture\":\"brand1.jpg\",\n" +
                "    \"brandAliases\":[\n" +
                "      \"brandA\"\n" +
                "      ,\"brandB\"\n" +
                "    ]\n" +
                "  }\n");
        BrandPojo brand = PojoUtils.json2Brand(json);
        assertNotNull(brand);
        assertEquals(brand.getBrandDesc(), "brand description");
        assertEquals(brand.getBrandAliases().size(), 2);
    }
}