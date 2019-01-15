package com.aizhixin.baobaorisk.redpackage.controller;

import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.redpackage.service.TradeRecordService;
import com.aizhixin.baobaorisk.redpackage.vo.TradeRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open/v1/trade")
@Api(description = "交易记录API")
public class TradeRecordController {

    @Autowired
    private TradeRecordService tradeRecordService;

    @GetMapping(value = "/{openId}/{pageNumber}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "交易记录查询", notes = "交易记录查询<br><br><b>@author zhen.pan</b>")
    public PageData<TradeRecordVO> query(@ApiParam(value = "openId", required = true) @PathVariable String openId,
                                        @ApiParam(value = "pageNumber", required = true) @PathVariable Integer pageNumber,
                                        @ApiParam(value = "pageSize", required = true) @PathVariable Integer pageSize) {
        return tradeRecordService.query(openId, pageNumber, pageSize);
    }
}
