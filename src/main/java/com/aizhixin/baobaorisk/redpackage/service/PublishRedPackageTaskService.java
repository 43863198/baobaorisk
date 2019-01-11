package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.common.tools.PageUtil;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.manager.RedTaskManager;
import com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageCountVO;
import com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Transactional
public class PublishRedPackageTaskService {
    @Autowired
    private RedTaskManager redTaskManager;

    /**
     * 已发布红包任务列表的查询
     */
    @Transactional(readOnly = true)
    public PageData<PublishRedPackageVO> queryPublish(String openId, Integer pageNumber, Integer pageSize) {
        PageData<PublishRedPackageVO> page = new PageData<>();
        PageRequest pageRequest = PageUtil.createNoErrorPageRequest(pageNumber, pageSize);
        page.getPage().setPageNumber(pageRequest.getPageNumber() + 1);
        page.getPage().setPageSize(pageRequest.getPageSize());
        Page<RedTask> p = redTaskManager.findByOpenId(pageRequest, openId);
        if (null != p) {
            page.getPage().setTotalElements(p.getTotalElements());
            page.getPage().setTotalPages(p.getTotalPages());
            List<PublishRedPackageVO> list = new ArrayList<>();
            Map<String, PublishRedPackageVO> map = new HashMap<>();
            for (RedTask r : p.getContent()) {
                PublishRedPackageVO v = new PublishRedPackageVO(r.getId(), r.getTaskName(), null == r.getTotalFee()? 0.0: r.getTotalFee()/100.0, r.getNum(), r.getCreatedDate(), r.getRedStatus());
                switch (r.getRedStatus()) {
                    case 20:
                        map.put(r.getId(), v);
                        break;
                    case 80:
                        v.setCompleteNum(v.getNum());
                        break;
                    default:
                        v.setCompleteNum(0);
                }
                list.add(v);
            }
            //计算任务中红包完成数量，并且回填数量
            if (!map.isEmpty()) {
            }
            page.setData(list);
        }
        return page;
    }

    @Transactional(readOnly = true)
    public PublishRedPackageCountVO countPublish(String openId) {
        return redTaskManager.countByOpenId(openId);
    }

}
