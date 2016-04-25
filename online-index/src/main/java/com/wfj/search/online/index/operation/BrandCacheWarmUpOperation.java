package com.wfj.search.online.index.operation;

import com.wfj.search.online.common.pojo.BrandPojo;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.util.record.pojo.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 品牌缓存预热操作
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
@Component("brandCacheWarmUpOperation")
public class BrandCacheWarmUpOperation implements IOperation<Void> {
    private boolean running = false;
    @Autowired
    private IPcmRequester pcmRequester;

    @Override
    public Void operate(Operation operation) throws Exception {
        try {
            running = true;
            List<BrandPojo> brandPojos = pcmRequester.listBrands();
            for (BrandPojo brandPojo : brandPojos) {
                pcmRequester.directGetBrandInfo(brandPojo.getBrandId());
            }
            return null;
        } finally {
            running = false;
        }

    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
