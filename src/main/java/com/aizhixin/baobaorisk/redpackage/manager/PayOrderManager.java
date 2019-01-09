package com.aizhixin.baobaorisk.redpackage.manager;

import com.aizhixin.baobaorisk.redpackage.entity.PayOrder;
import com.aizhixin.baobaorisk.redpackage.repository.PayOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class PayOrderManager {
    @Autowired
    private PayOrderRepository payOrderRepository;

    public PayOrder save(PayOrder entity) {
        return payOrderRepository.save(entity);
    }
}
