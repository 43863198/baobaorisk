package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.entity.GrapRedTask;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface GrapRedTaskRepository extends JpaRepository<GrapRedTask, String> {
    Page<GrapRedTask> findByRedTaskAndDeleteFlagOrderByCreatedDate(Pageable pageable, RedTask redTask, Integer deleteFlag);

    Page<GrapRedTask> findByRedTask_IdAndDeleteFlagOrderByCreatedDate(Pageable pageable, String taskId, Integer deleteFlag);

    Long countByRedTaskAndTaskStatusAndDeleteFlag(RedTask redTask, int taskStatus, int deleteFlag);

    @Modifying
    @Query("UPDATE #{#entityName} t SET t.taskStatus = 80 WHERE t.redTask = :redTask AND t.taskStatus = :taskStatus AND t.deleteFlag = :deleteFlag")
    void updateByRedTaskAndTaskStatusAndDeleteFlag(@Param(value = "redTask") RedTask redTask, @Param(value = "taskStatus") int taskStatus, @Param(value = "deleteFlag") int deleteFlag);
}
