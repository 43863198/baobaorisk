package com.aizhixin.baobaorisk.common.controller;

import com.aizhixin.baobaorisk.common.qiniu.QiniuHelper;
import com.aizhixin.baobaorisk.redpackage.vo.OneStrVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open/v1")
@Api(description = "工具API")
public class ToolController {
    @Autowired
    private QiniuHelper qiniuHelper;

    @PutMapping(value = "/qiniu/token/{openId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "七牛上传图片Token获取", notes = "七牛上传图片Token获取<br><br><b>@author zhen.pan</b>")
    public OneStrVO token(@ApiParam(value = "用户openId", required = true) @PathVariable String openId) {
        OneStrVO vo = new OneStrVO ();
        vo.setName(qiniuHelper.getToken());
        return vo;
    }
}
