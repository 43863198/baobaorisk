package com.aizhixin.baobaorisk.redpackage.controller;

import com.aizhixin.baobaorisk.common.vo.StatusVO;
import com.aizhixin.baobaorisk.common.wxbasic.WeixinService;
import com.aizhixin.baobaorisk.redpackage.vo.OpenIdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open/v1")
@Api(description = "微信基本操作API")
public class ServerBaseController {

    @Autowired
    private WeixinService weixinService;

    @PostMapping(value = "/live", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "服务活动验证测试", notes = "服务活动验证测试<br><br><b>@author zhen.pan</b>")
    public StatusVO live(@ApiParam(value = "appId", required = true) @RequestParam (value = "appId") String appId,
                         @ApiParam(value = "appKey", required = true) @RequestParam (value = "appKey") String appKey) {
        return new StatusVO();
    }

    @GetMapping(value = "/openId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取微信用户的OpenId", notes = "获取微信用户的OpenId<br><br><b>@author zhen.pan</b>")
    public OpenIdVO live(@ApiParam(value = "code", required = true) @RequestParam (value = "code") String code) {
        OpenIdVO o = new OpenIdVO ();
        String openId = weixinService.getWeixinOpenInfo(code);
        o.setOpenId(openId);
        return o;
    }
}
