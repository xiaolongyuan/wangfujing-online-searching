package com.wfj.search.online.index.cron;

import com.wfj.search.online.index.iao.IItemIAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>create at 15-12-18</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SpellRebuildJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemIAO itemIAO;

    public void rebuildSpell() {
        try {
            itemIAO.rebuildSpell();
        } catch (Exception e) {
            logger.warn("重建拼写检查失败", e);
        }
    }
}
