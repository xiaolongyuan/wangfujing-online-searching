package com.wfj.search.online.management.console.service.blacklist.impl;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.common.pojo.blacklist.BlacklistPojo;
import com.wfj.search.online.common.pojo.blacklist.BlacklistRecordPojo;
import com.wfj.search.online.management.console.mapper.blacklist.BlackListMapper;
import com.wfj.search.online.management.console.mapper.blacklist.BlacklistRecordMapper;
import com.wfj.search.online.management.console.service.ICacheService;
import com.wfj.search.online.management.console.service.blacklist.IBlackListService;
import com.wfj.search.online.management.console.service.index.IBrandIndexService;
import com.wfj.search.online.management.console.service.index.IItemIndexService;
import com.wfj.search.online.management.console.service.index.ISkuIndexService;
import com.wfj.search.online.management.console.service.index.ISpuIndexService;
import com.wfj.search.utils.signature.json.rsa.JsonSigner;
import com.wfj.search.utils.signature.json.rsa.StandardizingUtil;
import com.wfj.search.utils.signature.ras.KeyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import static com.wfj.search.online.common.pojo.blacklist.BlacklistPojo.Type.*;

/**
 * <br/>create at 15-10-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("blackListService")
public class BlackListServiceImpl implements IBlackListService, ResourceLoaderAware {
    private static final Logger logger = LoggerFactory.getLogger(BlackListServiceImpl.class);
    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private BlacklistRecordMapper blacklistRecordMapper;
    @Autowired
    private IItemIndexService itemIndexService;
    @Autowired
    private ISkuIndexService skuIndexService;
    @Autowired
    private ISpuIndexService spuIndexService;
    @Autowired
    private IBrandIndexService brandIndexService;
    @Autowired
    private ICacheService cacheService;
    private ResourceLoader resourceLoader;
    @Value("${search.caller}")
    private String caller;
    @Value("${search.privateKey}")
    private String privateKeyFile;
    private PrivateKey privateKey;

    @PostConstruct
    public void postConstruct() throws IOException, InvalidKeySpecException {
        String keyString = IOUtils.toString(resourceLoader.getResource(privateKeyFile).getInputStream());
        privateKey = KeyUtils.base64String2RSAPrivateKey(keyString);
    }

    @Override
    public int add(String id, String type, String modifier) {
        int flag = 0x00;
        if (StringUtils.isBlank(id) || StringUtils.isBlank(type) || StringUtils.isBlank(modifier)
                || !ArrayUtils.contains(BlacklistPojo.typeArray, type)) {
            return flag;
        }
        int count = blackListMapper.getCount(id, type);
        if (count > 0) {
            return flag;
        }
        blackListMapper.add(id, type, modifier);
        this.cacheService.clearBlacklistCacheOfType(type);// 清空对应类型黑名单缓存
        addRecord(id, type, modifier, BlacklistRecordPojo.ModifyType.ADD);
        flag = flag | BlacklistPojo.SUCCESS_DATABASE;

        JSONObject params = new JSONObject();
        JSONObject json;
        try {
            if (ITEM.getType().equals(type)) {
                params.put("itemId", id);
                json = itemIndexService.removeItem(
                        StandardizingUtil.standardize(JsonSigner.wrapSignature(params, privateKey, caller, "")));
            } else if (SKU.getType().equals(type)) {
                params.put("skuId", id);
                json = skuIndexService.removeItems(StandardizingUtil.standardize(JsonSigner.wrapSignature(params, privateKey, caller, "")));
            } else if (SPU.getType().equals(type)) {
                params.put("spuId", id);
                json = spuIndexService.removeItems(StandardizingUtil.standardize(JsonSigner.wrapSignature(params, privateKey, caller, "")));
            } else if (BRAND.getType().equals(type)) {
                params.put("brandId", id);
                json = brandIndexService.removeItems(StandardizingUtil.standardize(JsonSigner.wrapSignature(params, privateKey, caller, "")));
            } else {
                return flag;
            }
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            logger.error("签名过程发生错误，请检查签名组件是否正确！", e);
            return flag;
        }
        if (json != null && json.containsKey("success") && Boolean.parseBoolean(json.getString("success"))) {
            flag = flag | BlacklistPojo.SUCCESS_INDEX;
        }
        return flag;
    }

    @Override
    public int del(String id, String type, String modifier) {
        int flag = 0x00;
        if (StringUtils.isBlank(id) || StringUtils.isBlank(type) || StringUtils.isBlank(modifier)
                || !ArrayUtils.contains(BlacklistPojo.typeArray, type)) {
            return flag;
        }
        int count = blackListMapper.getCount(id, type);
        if (count == 0) {
            return flag;
        }
        blackListMapper.del(id, type);
        this.cacheService.clearBlacklistCacheOfType(type);// 清空对应类型黑名单缓存
        addRecord(id, type, modifier, BlacklistRecordPojo.ModifyType.DEL);
        flag = flag | BlacklistPojo.SUCCESS_DATABASE;

        JSONObject params = new JSONObject();
        JSONObject json;
        String message;
        try {
            message = StandardizingUtil.standardize(JsonSigner.wrapSignature(params, privateKey, caller, ""));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            logger.error("签名过程发生错误，请检查签名组件是否正确！", e);
            return -1;
        }
        if (ITEM.getType().equals(type)) {
            params.put("itemId", id);
            json = itemIndexService.refreshItem(message);
        } else if (SKU.getType().equals(type)) {
            params.put("skuId", id);
            json = skuIndexService.refreshItems(message);
        } else if (SPU.getType().equals(type)) {
            params.put("spuId", id);
            json = spuIndexService.refreshItems(message);
        } else if (BRAND.getType().equals(type)) {
            params.put("brandId", id);
            json = brandIndexService.refreshItems(message);
        } else {
            return -1;
        }
        if (json != null && json.containsKey("success") && Boolean.parseBoolean(json.getString("success"))) {
            flag = flag | BlacklistPojo.SUCCESS_INDEX;
        }
        return flag;
    }

    private void addRecord(String id, String type, String modifier, BlacklistRecordPojo.ModifyType modifyType) {
        try {
            BlacklistRecordPojo record = new BlacklistRecordPojo(id, type, modifier, modifyType);
            blacklistRecordMapper.addRecord(record);
        } catch (Exception e) {
            logger.error("添加黑名单操作记录失败，失败数据：id={},type={},modifier={},modifyTime={},modifyType={}",
                    id, type, modifier, modifyType.getType(), new Date(), e);
        }
    }

    @Override
    public List<BlacklistPojo> getBlackList(String id, String type, int start, int limit) {
        return blackListMapper.getBlacklist(id, type, start, limit);
    }

    @Override
    public int getCount(String id, String type) {
        return blackListMapper.getCount(id, type);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
