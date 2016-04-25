package com.wfj.search.online.index.operation;

import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.util.record.pojo.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分类缓存预热操作
 * <p>create at 16-4-25</p>
 *
 * @author liufl
 * @since 1.0.35
 */
@Component("categoryCacheWarmUpOperation")
public class CategoryCacheWarmUpOperation implements IOperation<Void> {
    private boolean running = false;
    @Autowired
    private IPcmRequester pcmRequester;

    @Override
    public Void operate(Operation operation) throws Exception {
        try {
            running = true;
            String cid = "0";
            this.warmUpSubCategories(cid);
            return null;
        } finally {
            running = false;
        }
    }

    private void warmUpSubCategories(String cid) throws RequestException {
        List<String> subIds = pcmRequester.listSubCategories(cid);
        if (subIds != null && !subIds.isEmpty()) {
            for (String subId : subIds) {
                pcmRequester.directGetCategoryInfo(subId);
                warmUpSubCategories(subId);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
