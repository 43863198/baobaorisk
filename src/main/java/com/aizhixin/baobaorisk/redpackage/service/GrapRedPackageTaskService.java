package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.core.PublicErrorCode;
import com.aizhixin.baobaorisk.common.exception.CommonException;
import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.common.tools.PageUtil;
import com.aizhixin.baobaorisk.redpackage.core.PicPublishStatus;
import com.aizhixin.baobaorisk.redpackage.core.RedPackageTaskStatus;
import com.aizhixin.baobaorisk.redpackage.entity.GrapRedTask;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.manager.GrapRedTaskManager;
import com.aizhixin.baobaorisk.redpackage.manager.RedTaskManager;
import com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    /**
     * 参与抢红包任务创建
     * 表单数据提交，同时提交用户微信信息数据
     */
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

    /**
     * 已抢红包列表
     * 做过的任务
     */
    @Transactional(readOnly = true)
    public PageData<GrapRedPackageTaskVO> queryGrapTask(String openId, Integer pageNumber, Integer pageSize) {
        PageData<GrapRedPackageTaskVO> page = new PageData<>();
        PageRequest pageRequest = PageUtil.createNoErrorPageRequest(pageNumber, pageSize);
        page.getPage().setPageNumber(pageRequest.getPageNumber() + 1);
        page.getPage().setPageSize(pageRequest.getPageSize());
        Page<GrapRedPackageTaskVO> p = grapRedTaskManager.queryGrapTask(pageRequest, openId);
        if (null != p) {
            page.setData(p.getContent());
            page.getPage().setTotalElements(p.getTotalElements());
            page.getPage().setTotalPages(p.getTotalPages());
        }
        return page;
    }
}
