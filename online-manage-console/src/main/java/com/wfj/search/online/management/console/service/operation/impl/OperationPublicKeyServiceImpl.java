package com.wfj.search.online.management.console.service.operation.impl;

import com.wfj.search.online.management.console.service.operation.IOperationPublicKeyService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * <br/>create at 15-12-9
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service
public class OperationPublicKeyServiceImpl implements IOperationPublicKeyService {
    @CacheEvict(value = "public-key-rsa-base64", allEntries = true)
    @Override
    public void clearPublicKey() {
    }
}
