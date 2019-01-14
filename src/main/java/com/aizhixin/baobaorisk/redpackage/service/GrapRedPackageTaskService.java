package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.core.PublicErrorCode;
import com.aizhixin.baobaorisk.common.exception.CommonException;
import com.aizhixin.baobaorisk.redpackage.core.PicPublishStatus;
import com.aizhixin.baobaorisk.redpackage.core.RedPackageTaskStatus;
import com.aizhixin.baobaorisk.redpackage.entity.GrapRedTask;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.manager.GrapRedTaskManager;
import com.aizhixin.baobaorisk.redpackage.manager.RedTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
public class GrapRedPackageTaskService {
    @Autowired
    private RedTaskManager redTaskManager;
    @Autowired
    private GrapRedTaskManager grapRedTaskManager;

    public String createGrapRedTask(String taskId, String picName, String remark, Integer isPublish, String nick, String openId, String avatar) {
        RedTask t = redTaskManager.findById(taskId);
        if (null == t) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "根据ID没有查询到相应的红包任务");
        }
        if (RedPackageTaskStatus.TASKING.getStateCode() != t.getRedStatus()) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "红包任务不是进行中");
        }
        if (null != isPublish) {
            if (isPublish.intValue() != PicPublishStatus.OPEN.getStateCode() && isPublish.intValue() != PicPublishStatus.CLOSE.getStateCode()) {
                throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "图片是否所有参与者");
            }
        } else {
            isPublish = PicPublishStatus.OPEN.getStateCode();
        }
        GrapRedTask g = new GrapRedTask ();
        g.setRedTask(t);
        g.setAvatar(avatar);
        g.setNick(nick);
        g.setOpenId(openId);
        g.setPic(picName);
        g.setRemark(remark);
        g.setPicPublish(isPublish);
        g = grapRedTaskManager.save(g);
        return g.getId();
    }

}
