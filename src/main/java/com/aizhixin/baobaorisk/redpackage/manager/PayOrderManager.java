package com.aizhixin.baobaorisk.redpackage.manager;

import com.aizhixin.baobaorisk.redpackage.entity.PayOrder;
import com.aizhixin.baobaorisk.redpackage.repository.PayOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class PayOrderManager {
    @Autowired
    private PayOrderRepository payOrderRepository;

    public PayOrder save(PayOrder entity) {
        return payOrderRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public PayOrder findByPrepayId(String prepayId) {
        List<PayOrder> list = payOrderRepository.findByPrepayId(prepayId);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public PayOrder findByTradeNo(String tradeNo) {
        List<PayOrder> list = payOrderRepository.findByTradeNo(tradeNo);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
