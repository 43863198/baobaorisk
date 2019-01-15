package com.aizhixin.baobaorisk.redpackage.manager;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.aizhixin.baobaorisk.redpackage.dto.PublishPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.repository.RedTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RedTaskManager {
    @Autowired
    private RedTaskRepository redTaskRepository;

    public RedTask save(RedTask entity) {
        return redTaskRepository.save(entity);
    }

    public RedTask findById(String id) {
        Optional<RedTask> o = redTaskRepository.findById(id);
        return o.orElse(null);
    }

    public RedTask findByTradeNo(String tradeNo) {
        List<RedTask> list = redTaskRepository.findByTradeNo(tradeNo);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public Page<RedTask> findByOpenId(Pageable pageable, String openId) {
        return  redTaskRepository.findByOpenIdAndDeleteFlagOrderByCreatedDateDesc(pageable, openId, DataValidity.VALID.getState());
    }

    public PublishPackageCountDTO countByOpenId(String openId) {
        List<PublishPackageCountDTO> list =  redTaskRepository.countByOpenId(openId, DataValidity.VALID.getState());
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return new PublishPackageCountDTO(0L, 0L, 0L);
    }
}
