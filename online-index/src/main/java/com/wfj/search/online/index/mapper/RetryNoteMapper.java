package com.wfj.search.online.index.mapper;

import com.wfj.search.online.common.pojo.OnlineRetryNotePojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static com.wfj.search.online.common.pojo.OnlineRetryNotePojo.*;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface RetryNoteMapper {
    List<OnlineRetryNotePojo> listUnresolvedNote(@Param("step") Step step, @Param("type") Type type,
            @Param("action") Action action);

    OnlineRetryNotePojo getRetryNote(String code);

    int addRetryNote(@Param("code") String code, @Param("step") Step step,
            @Param("type") Type type, @Param("action") Action action);

    int unresolvedRetryNote(@Param("code") String code, @Param("step") Step step,
            @Param("type") Type type, @Param("action") Action action);

    int rewriteRetryNote(@Param("code") String code, @Param("resolved") boolean resolved,
            @Param("step") Step step, @Param("type") Type type, @Param("action") Action action,
            @Param("version") long version);

    int cleanRetryNote(@Param("code") String code, @Param("comment") String comment, @Param("type") Type type);
}
