package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.dto.PublishPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageCountVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RedTaskRepository extends JpaRepository<RedTask, String> {
    List<RedTask> findByTradeNo(String tradeNo);

    Page<RedTask> findByOpenIdAndDeleteFlagOrderByCreatedDateDesc(Pageable pageable, String openId, Integer deleteFlag);

//    @Query("SELECT new com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageCountVO(COUNT(*), ROUND(SUM(t.totalFee)/100.0, 2), SUM(t.num)) FROM #{#entityName} t WHERE t.openId = :openId AND t.deleteFlag = :deleteFlag")
//    List<PublishRedPackageCountVO> countByOpenIdAndDeleteFlag(@Param(value = "openId")String openId, @Param(value = "deleteFlag")Integer deleteFlag);

    @Query("SELECT new com.aizhixin.baobaorisk.redpackage.dto.PublishPackageCountDTO(COUNT(*), SUM(t.totalFee), SUM(t.num))  FROM #{#entityName} t WHERE t.openId = :openId AND t.deleteFlag = :deleteFlag")
    List<PublishPackageCountDTO> countByOpenId(String openId, Integer deleteFlag);
}
