package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PayOrderRepository extends JpaRepository<PayOrder, String> {
    List<PayOrder> findByPrepayId(String prepayId);

    List<PayOrder> findByTradeNo(String tradeNo);
}
