package com.aizhixin.baobaorisk.common.controller;

import com.aizhixin.baobaorisk.common.exception.CommonException;
import com.aizhixin.baobaorisk.redpackage.service.PublishRedPackageTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manual/v1")
@Api(description = "手工操作API")
public class ManuController {
    @Autowired
    private PublishRedPackageTaskService publishRedPackageTaskService;

    @PutMapping(value = "/exception/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "异常测试", notes = "异常测试<br><br><b>@author zhen.pan</b>")
    public void exception(@ApiParam(value = "手机号码", required = true) @PathVariable String phone) {
        throw new CommonException(22222, "未知异常");
    }

    @GetMapping(value = "/wx/code", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "小程序二维码生成测试", notes = "小程序二维码生成测试<br><br><b>@author zhen.pan</b>")
    public void codeWX(@ApiParam(value = "任务ID") @RequestParam(value = "id", required = false) String id,
            @ApiParam(value = "小程序路径") @RequestParam(value = "path", required = false) String path) {
        publishRedPackageTaskService.getWxCode(id, path, 0);
    }
}
