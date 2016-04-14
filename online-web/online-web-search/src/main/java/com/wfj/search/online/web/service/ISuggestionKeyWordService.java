package com.wfj.search.online.web.service;

import java.util.List;

/**
 * <br/>create at 15-12-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ISuggestionKeyWordService {
    List<String> prefix();

    List<String> prefix(String prefix, String... word);
}
