package com.aizhixin.baobaorisk.common.controller;

import com.aizhixin.baobaorisk.common.exception.CommonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manual/v1")
@Api(description = "手工操作API")
public class ManuController {
    @PutMapping(value = "/exception/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "异常测试", notes = "异常测试<br><br><b>@author zhen.pan</b>")
    public void exception(@ApiParam(value = "手机号码", required = true) @PathVariable String phone) {
        throw new CommonException(22222, "未知异常");
    }
}
