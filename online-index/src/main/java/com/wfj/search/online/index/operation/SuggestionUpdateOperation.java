package com.wfj.search.online.index.operation;

import com.google.common.collect.Lists;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.iao.QueryException;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.pojo.SuggestionIndexPojo;
import com.wfj.search.online.index.service.IEsService;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.online.index.service.ISuggestionIndexService;
import com.wfj.search.util.record.pojo.Operation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品价格变化抓取任务
 * <p>create at 16-1-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("suggestionUpdateOperation")
public class SuggestionUpdateOperation implements IOperation<Void> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean running = false;
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;
    @Autowired
    private ISuggestionIndexService suggestionIndexService;

    public synchronized Void operate(Operation opRecord)
            throws IndexException, RequestException, InterruptedException {
        this.running = true;
        try {
            StringBuilder msg = new StringBuilder();
            List<String> channels = null;
            try {
                channels = this.indexService.facetChannels();
            } catch (QueryException e) {
                logger.error("提取可用渠道号失败", e);
                msg.append("提取可用渠道号失败, ").append(e.toString());
            }
            if (channels != null) {
                List<SuggestionIndexPojo> suggestionIndexPojos = null;
                for (String channel : channels) {
                    try {
                        suggestionIndexPojos = this.indexService.facetAllItemKeywords(channel);
                    } catch (QueryException e) {
                        logger.error("提取商品keyword失败", e);
                        msg.append("提取商品Keyword失败,").append(e.toString());
                    }
                    this.suggestionIndexService.save(suggestionIndexPojos, Long.parseLong(opRecord.getSid()))
                            .ifPresent(failure -> msg.append(failure.toString()));
                    try {
                        suggestionIndexPojos = this.esService.aggregateSearchQueries(channel);
                    } catch (Exception e) {
                        logger.error("提取搜索词历史失败", e);
                        msg.append("提取搜索词历史失败,").append(e.toString());
                    }
                    if (suggestionIndexPojos != null) {
                        List<SuggestionIndexPojo> historyRecords = suggestionIndexPojos;
                        suggestionIndexPojos = Lists.newArrayList();
                        for (SuggestionIndexPojo historyRecord : historyRecords) {
                            try {
                                this.indexService.fillKeywordMatches(historyRecord);
                                suggestionIndexPojos.add(historyRecord);
                            } catch (QueryException e) {
                                logger.error("查询Keyword[{}]匹配结果数失败", historyRecord.getKeyword(), e);
                            }
                        }
                    }
                    this.suggestionIndexService.save(suggestionIndexPojos, Long.parseLong(opRecord.getSid()))
                            .ifPresent(failure -> msg.append(failure.toString()));
                }
                this.suggestionIndexService.removeExpired(Long.parseLong(opRecord.getSid()))
                        .ifPresent(failure -> msg.append(failure.toString()));
                this.suggestionIndexService.commit()
                        .ifPresent(failure -> msg.append(failure.toString()));
            }
            String comment = opRecord.getComment();
            if (StringUtils.isBlank(comment)) {
                opRecord.setComment(msg.toString());
            } else {
                if (comment.endsWith("\n")) {
                    opRecord.setComment(comment + msg);
                } else {
                    opRecord.setComment(comment + '\n' + msg);
                }
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
