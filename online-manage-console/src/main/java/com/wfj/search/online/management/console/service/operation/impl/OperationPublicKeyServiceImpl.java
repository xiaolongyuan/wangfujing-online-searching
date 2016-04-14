package com.wfj.search.online.management.console.service.operation.impl;

import com.wfj.search.online.management.console.service.operation.IOperationPublicKeyService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static com.wfj.platform.operation.record.plugin.common.OperationConstants.CACHE_PUBLIC_KEY_PROVIDER;

/**
 * <br/>create at 15-12-9
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service
public class OperationPublicKeyServiceImpl implements IOperationPublicKeyService {
    @CacheEvict(value = CACHE_PUBLIC_KEY_PROVIDER, allEntries = true)
    @Override
    public void clearPublicKey() {
    }
}
