package com.wfj.search.online.web.es;

import com.wfj.search.online.index.pojo.ColorIndexPojo;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface ColorEsIao {
    ColorIndexPojo get(String colorId);
}
