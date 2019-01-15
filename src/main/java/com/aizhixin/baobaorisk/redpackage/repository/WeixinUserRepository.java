package com.aizhixin.baobaorisk.redpackage.repository;


import com.aizhixin.baobaorisk.redpackage.entity.WeixinUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WeixinUserRepository extends JpaRepository<WeixinUser, String> {
    List<WeixinUser> findByOpenId(String openId);
}
