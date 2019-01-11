package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RedTaskRepository extends JpaRepository<RedTask, String> {
    List<RedTask> findByTradeNo(String tradeNo);
}
