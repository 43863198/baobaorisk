package com.aizhixin.baobaorisk.redpackage.manager;

import com.aizhixin.baobaorisk.redpackage.entity.PayOrder;
import com.aizhixin.baobaorisk.redpackage.repository.PayOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayOrderManager {
    @Autowired
    private PayOrderRepository payOrderRepository;

    public PayOrder save(PayOrder entity) {
        return payOrderRepository.save(entity);
    }

    public PayOrder findByTradeNo(String tradeNo) {
        List<PayOrder> list = payOrderRepository.findByTradeNo(tradeNo);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
