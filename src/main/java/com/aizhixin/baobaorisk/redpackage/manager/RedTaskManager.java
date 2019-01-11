package com.aizhixin.baobaorisk.redpackage.manager;

import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.repository.RedTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class RedTaskManager {
    @Autowired
    private RedTaskRepository redTaskRepository;

    public RedTask save(RedTask entity) {
        return redTaskRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public RedTask findByTradeNo(String tradeNo) {
        List<RedTask> list = redTaskRepository.findByTradeNo(tradeNo);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
