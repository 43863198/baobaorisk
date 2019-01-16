package com.aizhixin.baobaorisk.redpackage.manager;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.aizhixin.baobaorisk.redpackage.core.TradeType;
import com.aizhixin.baobaorisk.redpackage.dto.WithDrawCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.TradeRecord;
import com.aizhixin.baobaorisk.redpackage.repository.TradeRecordRepository;
import com.aizhixin.baobaorisk.redpackage.vo.TradeRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradeRecordManager {
    @Autowired
    private TradeRecordRepository tradeRecordRepository;

    public TradeRecord save(TradeRecord entity) {
        return tradeRecordRepository.save(entity);
    }

    public TradeRecord findByOpenIdAndTradeNo(String openId, String tradeNo) {
        List<TradeRecord> list = tradeRecordRepository.findByOpenIdAndTradeNoAndDeleteFlag(openId, tradeNo, DataValidity.VALID.getState());
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public Page<TradeRecordVO> findByOpenId(Pageable pageable, String openId) {
        return  tradeRecordRepository.findByOpenIdAndDeleteFlagOrderByCreatedDateDesc(pageable, openId, DataValidity.VALID.getState());
    }


    public WithDrawCountDTO countFeeByOpenId(String openId) {
        List<WithDrawCountDTO> list = tradeRecordRepository.countFeeByOpenIdAndDeleteFlag(openId, TradeType.WX_WITHDRAW.getStateCode(), DataValidity.VALID.getState());
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return new WithDrawCountDTO (0L, 0L);
    }
}
