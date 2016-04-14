package com.wfj.search.online.statistics.service.impl;

import com.wfj.search.online.statistics.es.ClicksEsIao;
import com.wfj.search.online.statistics.mapper.ClickMapper;
import com.wfj.search.online.statistics.pojo.ClickCountPojo;
import com.wfj.search.online.statistics.pojo.ClickPojo;
import com.wfj.search.online.statistics.service.IClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>create at 15-12-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("clickService")
public class ClickServiceImpl implements IClickService {
    @Autowired
    private ClickMapper clickMapper;
    @Autowired
    private ClicksEsIao clicksEsIao;

    @Override
    public void onClick(ClickPojo clickPojo) {
        this.clickMapper.save(clickPojo);
        int count = this.clickMapper.countClickOfSpu(clickPojo.getSpu_id());
        ClickCountPojo clickCountPojo = new ClickCountPojo();
        clickCountPojo.setSpuClick(count);
        clickCountPojo.setSpuId(clickPojo.getSpu_id());
        this.clicksEsIao.upsert(clickCountPojo);
    }
}
