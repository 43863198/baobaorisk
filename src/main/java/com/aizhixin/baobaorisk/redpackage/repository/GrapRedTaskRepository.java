package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.entity.GrapRedTask;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GrapRedTaskRepository extends JpaRepository<GrapRedTask, String> {
    Page<GrapRedTask> findByRedTaskAndDeleteFlagOrderByCreatedDate(Pageable pageable, RedTask redTask, Integer deleteFlag);

    Page<GrapRedTask> findByRedTask_IdAndDeleteFlagOrderByCreatedDate(Pageable pageable, String taskId, Integer deleteFlag);

//    @Query("select new com.aizhixin.baobaorisk.redpackage.dto.RedPackageCountDTO(count(*), sum(IF(20 = taskStatus OR 30 == taskStatus, 1, 0)), sum(IF(20 = taskStatus, 1, 0)), sum(t.totalFee)) from #{#entityName} t where t.redTask = :redTask and t.deleteFlag = :deleteFlag")
//    List<RedPackageCountDTO> countGrapRedNumByRedTaskAndDeleteFlag(@Param(value = "redTask") RedTask redTask, @Param(value = "deleteFlag") int deleteFlag);
//
//    @Query("select new com.aizhixin.baobaorisk.redpackage.dto.RedPackageCountDTO(count(*), sum(IF(20 = taskStatus OR 30 == taskStatus, 1, 0)), sum(IF(20 = taskStatus, 1, 0)), sum(t.totalFee)) from #{#entityName} t where t.redTask.id = :taskId and t.deleteFlag = :deleteFlag")
//    List<RedPackageCountDTO> countGrapRedNumByRedTaskIdAndDeleteFlag(@Param(value = "taskId") String taskId, @Param(value = "deleteFlag") int deleteFlag);

}
