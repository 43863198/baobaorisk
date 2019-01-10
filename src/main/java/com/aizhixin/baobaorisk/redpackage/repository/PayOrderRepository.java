package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PayOrderRepository extends JpaRepository<PayOrder, String> {
}
