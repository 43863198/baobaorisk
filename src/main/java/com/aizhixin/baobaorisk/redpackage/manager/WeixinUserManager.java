package com.aizhixin.baobaorisk.redpackage.manager;

import com.aizhixin.baobaorisk.redpackage.entity.WeixinUser;
import com.aizhixin.baobaorisk.redpackage.repository.WeixinUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeixinUserManager {
    @Autowired
    private WeixinUserRepository weixinUserRepository;

    public WeixinUser save(WeixinUser entity) {
        return weixinUserRepository.save(entity);
    }


    public WeixinUser findByOpenId(String openId) {
        List<WeixinUser> list = weixinUserRepository.findByOpenId(openId);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
