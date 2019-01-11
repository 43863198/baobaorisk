package com.aizhixin.baobaorisk.redpackage.controller;

import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.redpackage.service.PayService;
import com.aizhixin.baobaorisk.redpackage.service.PublishRedPackageTaskService;
import com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageCountVO;
import com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageVO;
import com.aizhixin.baobaorisk.redpackage.vo.WxCreatePayVO;
import com.aizhixin.baobaorisk.redpackage.vo.WxPrePayVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open/v1")
@Api(description = "红包及支付相关操作API")
public class PublishRedPackageTaskController {
    @Autowired
    private PayService payService;
    @Autowired
    private PublishRedPackageTaskService publishRedPackageTaskService;

    @PostMapping(value = "/order/{openId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "红包充值", notes = "红包充值<br><br><b>@author zhen.pan</b>")
    public WxPrePayVO order(@ApiParam(value = "openId", required = true) @PathVariable String openId,
                            @ApiParam(value = "红包总金额(大于0，小于200)<br/>红包数量(大于0，小于100)<br/>红包充值,总金额/红包数量 >= 0.01", required = true) @RequestBody WxCreatePayVO payVo) {
        return payService.createOrder(openId, payVo.getTotalFee(), payVo.getNum(), payVo.getRemark());
    }

    @GetMapping(value = "/publish/{openId}/{pageNumber}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "已发布红包任务的分页查询", notes = "已发布红包任务的分页查询<br><br><b>@author zhen.pan</b>")
    public PageData<PublishRedPackageVO> publish(@ApiParam(value = "openId", required = true) @PathVariable String openId,
                                                 @ApiParam(value = "pageNumber", required = true) @PathVariable Integer pageNumber,
                                                 @ApiParam(value = "pageSize", required = true) @PathVariable Integer pageSize) {
        return publishRedPackageTaskService.queryPublish(openId, pageNumber, pageSize);
    }

    @GetMapping(value = "/publish/{openId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "已发布红包任务的合计值查询", notes = "已发布红包任务的合计值查询<br><br><b>@author zhen.pan</b>")
    public PublishRedPackageCountVO publish(@ApiParam(value = "openId", required = true) @PathVariable String openId) {
        return publishRedPackageTaskService.countPublish(openId);
    }
}
