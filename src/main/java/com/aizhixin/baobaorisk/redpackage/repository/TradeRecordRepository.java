package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.dto.WithDrawCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.TradeRecord;
import com.aizhixin.baobaorisk.redpackage.vo.TradeRecordVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TradeRecordRepository extends JpaRepository<TradeRecord, String> {
    List<TradeRecord> findByOpenIdAndTradeNoAndDeleteFlag(String openId, String tradeNo, Integer deleteFlag);
    @Query("SELECT new com.aizhixin.baobaorisk.redpackage.vo.TradeRecordVO(t.id, t.tradeName, t.cashFee, t.discountFee, t.timeEnd, t.tradeType) FROM #{#entityName} t WHERE t.openId = :openId AND t.deleteFlag = :deleteFlag ORDER BY t.createdDate")
    Page<TradeRecordVO> findByOpenIdAndDeleteFlagOrderByCreatedDateDesc(Pageable pageable, @Param(value = "openId") String openId, @Param(value = "deleteFlag") Integer deleteFlag);

    @Query("SELECT new com.aizhixin.baobaorisk.redpackage.dto.WithDrawCountDTO(SUM(t.cashFee), SUM(t.discountFee)) FROM #{#entityName} t WHERE t.openId = :openId AND t.tradeType = :tradeType AND t.deleteFlag = :deleteFlag")
    List<WithDrawCountDTO> countFeeByOpenIdAndDeleteFlag( @Param(value = "openId") String openId, @Param(value = "tradeType") Integer tradeType, @Param(value = "deleteFlag") Integer deleteFlag);
}
