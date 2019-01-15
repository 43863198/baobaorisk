package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.common.tools.PageUtil;
import com.aizhixin.baobaorisk.redpackage.manager.TradeRecordManager;
import com.aizhixin.baobaorisk.redpackage.vo.TradeRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
public class TradeRecordService {
    @Autowired
    private TradeRecordManager tradeRecordManager;

    @Transactional(readOnly = true)
    public PageData<TradeRecordVO> query(String openId, Integer pageNumber, Integer pageSize) {
        PageData<TradeRecordVO> page = new PageData<>();
        PageRequest pageRequest = PageUtil.createNoErrorPageRequest(pageNumber, pageSize);
        page.getPage().setPageNumber(pageRequest.getPageNumber() + 1);
        page.getPage().setPageSize(pageRequest.getPageSize());
        Page<TradeRecordVO> p = tradeRecordManager.findByOpenId(pageRequest, openId);
        if (null != p) {
            page.getPage().setTotalElements(p.getTotalElements());
            page.getPage().setTotalPages(p.getTotalPages());
            page.setData(p.getContent());
        }
        return page;
    }
}
