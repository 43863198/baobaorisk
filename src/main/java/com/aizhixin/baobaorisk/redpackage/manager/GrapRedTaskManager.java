package com.aizhixin.baobaorisk.redpackage.manager;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.aizhixin.baobaorisk.redpackage.core.GrapRedPackageStatus;
import com.aizhixin.baobaorisk.redpackage.dto.GrapPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.dto.RedPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.GrapRedTask;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.repository.GrapRedTaskRepository;
import com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageTaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Component
public class GrapRedTaskManager {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GrapRedTaskRepository grapRedTaskRepository;

    public GrapRedTask save(GrapRedTask entity) {
        return grapRedTaskRepository.save(entity);
    }

    public List<GrapRedTask> saveAll(List<GrapRedTask> entity) {
        return grapRedTaskRepository.saveAll(entity);
    }

    public GrapRedTask findById(String id) {
        Optional<GrapRedTask> o = grapRedTaskRepository.findById(id);
        return o.orElse(null);
    }

    public Page<GrapRedTask> findByRedTask(Pageable pageable, RedTask redTask) {
        return grapRedTaskRepository.findByRedTaskAndDeleteFlagOrderByCreatedDate(pageable, redTask, DataValidity.VALID.getState());
    }

    public Page<GrapRedTask> findByRedTaskId(Pageable pageable, String taskId) {
        return grapRedTaskRepository.findByRedTask_IdAndDeleteFlagOrderByCreatedDate(pageable, taskId, DataValidity.VALID.getState());
    }

    public int countGrapVerifyPassedTask(RedTask redTask) {
        Long c = grapRedTaskRepository.countByRedTaskAndTaskStatusAndDeleteFlag(redTask, GrapRedPackageStatus.PASSED.getStateCode(), DataValidity.VALID.getState());
        if (null == c) {
            return 0;
        } else {
            return c.intValue();
        }
    }

    public int countByRedTaskAndOpenId(RedTask redTask, String openId) {
        Long c = grapRedTaskRepository.countByRedTaskAndOpenIdAndDeleteFlag(redTask, openId, DataValidity.VALID.getState());
        if (null == c) {
            return 0;
        } else {
            return c.intValue();
        }
    }

    public RedPackageCountDTO countGrapRedTask(String taskId) {
        List<RedPackageCountDTO> list = jdbcTemplate.query("SELECT COUNT(*) countNums, SUM(IF(t.TASK_STATUS = 20 OR t.TASK_STATUS = 30, 1, 0)) verifyNums, SUM(IF(t.TASK_STATUS = 20, 1, 0)) grapNums, SUM(IF(t.TASK_STATUS = 20, t.TOTAL_FEE, 0)) grapFee FROM t_grap_red_task t WHERE t.DELETE_FLAG=0 AND t.TASK_ID=?",
                new Object[] {taskId},
                new int[] {Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new RedPackageCountDTO(rs.getLong("countNums"),
                        rs.getLong("verifyNums"), rs.getLong("grapNums"), rs.getLong("grapFee")));
        if (null == list || list.isEmpty()) {
            return new RedPackageCountDTO(0L, 0L, 0L, 0L);
        }
        return list.get(0);
    }

    public void doInvalidGrapTask(RedTask redTask) {
        grapRedTaskRepository.updateByRedTaskAndTaskStatusAndDeleteFlag(redTask, GrapRedPackageStatus.INVALID.getStateCode(), DataValidity.VALID.getState());
    }

    public Page<GrapRedPackageTaskVO> queryGrapTask(Pageable pageable, String openId) {
        return grapRedTaskRepository.findByOpenIdAndDeleteFlagOrderByCreatedDate(pageable, openId, DataValidity.VALID.getState());
    }

    public GrapPackageCountDTO countByOpenId(String openId) {
        GrapPackageCountDTO d;
        List<GrapPackageCountDTO> list = grapRedTaskRepository.countByOpenIdAndTaskStatusAndDeleteFlag(openId, GrapRedPackageStatus.PASSED.getStateCode(), DataValidity.VALID.getState());
        if (null == list || list.isEmpty()) {
            d = new GrapPackageCountDTO(0L, 0L);
        } else {
            d = list.get(0);
        }
        d.setTasks(grapRedTaskRepository.countByOpenIdAndDeleteFlag(openId, DataValidity.VALID.getState()));
        return d;
    }
}
