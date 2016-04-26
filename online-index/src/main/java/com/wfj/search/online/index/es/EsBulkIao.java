package com.wfj.search.online.index.es;

import com.wfj.search.online.index.pojo.IndexPojos;
import com.wfj.search.online.index.pojo.failure.Failure;

import java.util.Optional;

/**
 * <p>create at 16-4-26</p>
 *
 * @author liufl
 * @since 1.0.35
 */
public interface EsBulkIao {
    Optional<Failure> bulkIndex(IndexPojos indexPojos, long version);
}
