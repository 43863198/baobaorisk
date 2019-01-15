package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.core.PublicErrorCode;
import com.aizhixin.baobaorisk.common.exception.CommonException;
import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.common.tools.PageUtil;
import com.aizhixin.baobaorisk.common.vo.MsgVO;
import com.aizhixin.baobaorisk.redpackage.core.PicPublishStatus;
import com.aizhixin.baobaorisk.redpackage.core.RedPackageTaskStatus;
import com.aizhixin.baobaorisk.redpackage.dto.GrapPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.GrapRedTask;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.entity.WeixinUser;
import com.aizhixin.baobaorisk.redpackage.manager.GrapRedTaskManager;
import com.aizhixin.baobaorisk.redpackage.manager.RedTaskManager;
import com.aizhixin.baobaorisk.redpackage.manager.WeixinUserManager;
import com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageCountVO;
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
    @Autowired
    private WeixinUserManager weixinUserManager;

    /**
     * 参与抢红包任务创建
     * 表单数据提交，同时提交用户微信信息数据
     */
    public MsgVO createGrapRedTask(String taskId, String picName, String remark, Integer isPublish, String nick, String openId, String avatar) {
        MsgVO vo = new MsgVO ();
        /***************************************业务逻辑验证*********************************************/
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
        if (grapRedTaskManager.countByRedTaskAndOpenId(t, openId) > 0) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "不能重复参与");
        }
        /***************************************创建参与红包任务数*********************************************/
        GrapRedTask g = new GrapRedTask ();
        g.setRedTask(t);
        g.setAvatar(avatar);
        g.setNick(nick);
        g.setOpenId(openId);
        g.setPic(picName);
        g.setRemark(remark);
        g.setPicPublish(isPublish);
        grapRedTaskManager.save(g);

        /***************************************更新用户任务数*********************************************/
        WeixinUser u = weixinUserManager.findByOpenId(openId);
        if (null == u) {
            u = new WeixinUser();
            u.setOpenId(openId);
        }
        u.setAvatar(avatar);
        u.setNick(nick);
        GrapPackageCountDTO c = grapRedTaskManager.countByOpenId(openId);
        u.setGrapTotalFee(c.getFees());
        u.setGrapRedNums(c.getReds());
        u.setGrapTaskNums(c.getTasks());
        weixinUserManager.save(u);
        return vo;
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


    /**
     * 总抢包任务统计
     */
    @Transactional(readOnly = true)
    public GrapRedPackageCountVO countPublish(String openId) {
        WeixinUser user = weixinUserManager.findByOpenId(openId);
        if (null != user) {
            return new GrapRedPackageCountVO(user.getNick(), user.getAvatar(), user.getGrapTaskNums(), user.getGrapTotalFee() / 100.0, user.getGrapRedNums());
        } else {
            return new GrapRedPackageCountVO("", "", 0L, 0.0, 0L);
        }
    }
}
