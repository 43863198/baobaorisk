package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.dto.GrapPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.GrapRedTask;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageListVO;
import com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageTaskVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GrapRedTaskRepository extends JpaRepository<GrapRedTask, String> {
//    Page<GrapRedTask> findByRedTaskAndDeleteFlagOrderByCreatedDate(Pageable pageable, RedTask redTask, Integer deleteFlag);

//    Page<GrapRedTask> findByRedTask_IdAndDeleteFlagOrderByCreatedDate(Pageable pageable, String taskId, Integer deleteFlag);

    @Query("SELECT new com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageListVO(t.redTask.id, t.id, t.pic, t.remark, t.picPublish, t.nick, t.avatar, t.totalFee, t.verifyDate, t.taskStatus) FROM #{#entityName} t WHERE t.redTask.id = :taskId AND t.deleteFlag = :deleteFlag ORDER BY t.createdDate")
    Page<GrapRedPackageListVO> findByRedTask_IdAndDeleteFlag(Pageable pageable, @Param(value = "taskId") String taskId, @Param(value = "deleteFlag") Integer deleteFlag);

//    Long countByRedTaskAndTaskStatusAndDeleteFlag(RedTask redTask, int taskStatus, int deleteFlag);

    long countByRedTaskAndOpenIdAndDeleteFlag(RedTask redTask, String openId, int deleteFlag);

    @Modifying
    @Query("UPDATE #{#entityName} t SET t.taskStatus = 80 WHERE t.redTask = :redTask AND t.taskStatus = :taskStatus AND t.deleteFlag = :deleteFlag")
    void updateByRedTaskAndTaskStatusAndDeleteFlag(@Param(value = "redTask") RedTask redTask, @Param(value = "taskStatus") int taskStatus, @Param(value = "deleteFlag") int deleteFlag);

    @Query("SELECT new com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageTaskVO(t.redTask.id, t.redTask.nick, t.redTask.avatar, t.redTask.taskName, t.redTask.createdDate, t.totalFee, t.taskStatus)  FROM #{#entityName} t WHERE t.openId = :openId AND t.deleteFlag = :deleteFlag ORDER BY t.createdDate")
    Page<GrapRedPackageTaskVO> findByOpenIdAndDeleteFlagOrderByCreatedDate(Pageable pageable, @Param(value = "openId") String openId, @Param(value = "deleteFlag") Integer deleteFlag);

    Long countByOpenIdAndDeleteFlag(String openId, int deleteFlag);

    @Query("SELECT new com.aizhixin.baobaorisk.redpackage.dto.GrapPackageCountDTO(SUM(t.totalFee), COUNT(*)) FROM #{#entityName} t WHERE t.openId = :openId AND t.taskStatus = :taskStatus AND t.deleteFlag = :deleteFlag")
    List<GrapPackageCountDTO> countByOpenIdAndTaskStatusAndDeleteFlag(@Param(value = "openId") String openId, @Param(value = "taskStatus") int taskStatus, @Param(value = "deleteFlag") int deleteFlag);
}
