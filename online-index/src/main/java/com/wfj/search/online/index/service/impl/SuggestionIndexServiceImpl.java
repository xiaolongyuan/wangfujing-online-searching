package com.wfj.search.online.index.service.impl;

import com.wfj.search.online.index.iao.ISuggestionIAO;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.SuggestionIndexPojo;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.service.ISuggestionIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>create at 16-1-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("suggestionIndexService")
public class SuggestionIndexServiceImpl implements ISuggestionIndexService {
    @Autowired
    private ISuggestionIAO suggestionIAO;

    @Override
    public Optional<Failure> save(List<SuggestionIndexPojo> suggestionIndexPojos, long version) {
        if (suggestionIndexPojos == null) {
            return Optional.empty();
        }
        suggestionIndexPojos.forEach(s -> s.setOperationSid(version));
        try {
            this.suggestionIAO.save(suggestionIndexPojos);
            return Optional.empty();
        } catch (IndexException e) {
            return Optional.of(new Failure(DataType.suggestion, FailureType.save2Index, "", "将建议词写入建议词库失败", e));
        }
    }

    @Override
    public Optional<Failure> removeExpired(long version) {
        try {
            this.suggestionIAO.removeExpired(version);
        } catch (IndexException e) {
            return Optional.of(new Failure(DataType.suggestion, FailureType.save2Index, "", "清除过期建议词失败", e));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Failure> commit() {
        try {
            this.suggestionIAO.commit();
        } catch (IndexException e) {
            return Optional.of(new Failure(DataType.suggestion, FailureType.save2Index, "", "Commit建议词词库失败", e));
        }
        return Optional.empty();
    }
}
