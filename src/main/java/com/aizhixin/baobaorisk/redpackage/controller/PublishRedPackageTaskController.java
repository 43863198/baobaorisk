package com.aizhixin.baobaorisk.redpackage.controller;

import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.common.vo.MsgVO;
import com.aizhixin.baobaorisk.redpackage.service.PayService;
import com.aizhixin.baobaorisk.redpackage.service.PublishRedPackageTaskService;
import com.aizhixin.baobaorisk.redpackage.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open/v1")
@Api(description = "发布红包及支付相关操作API")
public class PublishRedPackageTaskController {
    @Autowired
    private PayService payService;
    @Autowired
    private PublishRedPackageTaskService publishRedPackageTaskService;

    @PostMapping(value = "/order/{openId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "红包充值", notes = "红包充值<br><br><b>@author zhen.pan</b>")
    public WxPrePayVO order(@ApiParam(value = "openId", required = true) @PathVariable String openId,
                            @ApiParam(value = "红包总金额(大于0，小于200)<br/>红包数量(大于0，小于100)<br/>红包充值,总金额/红包数量 >= 0.01", required = true) @RequestBody WxCreatePayVO payVo) {
        return payService.createOrder(openId, payVo.getTotalFee(), payVo.getNum(), payVo.getRemark(), payVo.getNick(), payVo.getAvatar());
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

    @GetMapping(value = "/task/{taskId}/code", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "产生二维码文件", notes = "产生二维码文件<br><br><b>@author zhen.pan</b>")
    public OneStrVO task(@ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
                                         @ApiParam(value = "路径", required = true) @RequestParam(value = "path") String path,
                                         @ApiParam(value = "二维码宽带") @RequestParam(value = "width", required = false) Integer width) {
        OneStrVO vo = new OneStrVO ();
        vo.setName(publishRedPackageTaskService.getWxCode(taskId, path, width));
        return vo;
    }

    @GetMapping(value = "/task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取红包任务详情", notes = "获取红包任务详情<br><br><b>@author zhen.pan</b>")
    public PublishRedPackageDetailVO taskDetails(@ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        return publishRedPackageTaskService.getPublishRedPackageTask(taskId);
    }

    @GetMapping(value = "/task/{taskId}/avatar", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取红包任务发起人头像", notes = "获取红包任务发起人头像<br><br><b>@author zhen.pan</b>")
    public ResponseEntity<byte[]> taskAvatar(@ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        return publishRedPackageTaskService.getTaskAvatar(taskId);
    }

    @GetMapping(value = "/publish/{openId}/task/{taskId}/{pageNumber}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "抢红包参与者信息", notes = "抢红包参与者信息，按照参与时间排序<br><br><b>@author zhen.pan</b>")
    public PageData<GrapRedPackageListVO> publish(@ApiParam(value = "openId", required = true) @PathVariable String openId,
                                            @ApiParam(value = "taskId", required = true) @PathVariable String taskId,
                                            @ApiParam(value = "pageNumber", required = true) @PathVariable Integer pageNumber,
                                            @ApiParam(value = "pageSize", required = true) @PathVariable Integer pageSize) {
        return publishRedPackageTaskService.queryGrapTask(openId, taskId, pageNumber, pageSize);
    }

    @PutMapping(value = "/publish/{openId}/task/{taskId}/grap/{grapId}/{verifyStatus}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "审核参与通过或不通过", notes = "审核参与通过或不通过<br><br><b>@author zhen.pan</b>")
    public MsgVO doVerify(@ApiParam(value = "任务发起人openId", required = true) @PathVariable String openId,
                         @ApiParam(value = "红包任务taskId", required = true) @PathVariable String taskId,
                         @ApiParam(value = "抢包任务grapId", required = true) @PathVariable String grapId,
                         @ApiParam(value = "通过20，不通过30", required = true) @PathVariable Integer verifyStatus) {
        return publishRedPackageTaskService.doVerifyGrapTask(openId, taskId, grapId, verifyStatus);
    }

    @PutMapping(value = "/withdraw/{openId}/{fee}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "提现", notes = "提现<br><br><b>@author zhen.pan</b>")
    public MsgVO withdraw(@ApiParam(value = "openId", required = true) @PathVariable String openId,
                          @ApiParam(value = "提现金额", required = true) @PathVariable Double fee) {
        return payService.payToWeixinOne(openId, (int)(fee * 100));
    }

    @GetMapping(value = "/balance/{openId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "个人中心余额查询", notes = "个人中心余额查询<br><br><b>@author zhen.pan</b>")
    public BalanceCountVO withdraw(@ApiParam(value = "openId", required = true) @PathVariable String openId) {
        return payService.queryBalanceCount(openId);
    }
}
