package com.aizhixin.baobaorisk.redpackage.controller;

import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.common.vo.MsgVO;
import com.aizhixin.baobaorisk.redpackage.service.GrapRedPackageTaskService;
import com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageCountVO;
import com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageTaskVO;
import com.aizhixin.baobaorisk.redpackage.vo.GrapRedPackageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open/v1/grap")
@Api(description = "抢红包相关操作API")
public class GrapRedPackageTaskController {
    @Autowired
    private GrapRedPackageTaskService grapRedPackageTaskService;

    @PostMapping(value = "/{openId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "抢包任务创建", notes = "抢包任务创建<br><br><b>@author zhen.pan</b>")
    public MsgVO grapCreate(@ApiParam(value = "openId", required = true) @PathVariable String openId,
                       @ApiParam(value = "taskId 是必须的，填上表单数据和微信用户信息", required = true) @RequestBody GrapRedPackageVO grapVo) {
        MsgVO vo = new MsgVO ();
        String id = grapRedPackageTaskService.createGrapRedTask(grapVo.getTaskId(), grapVo.getPicName(), grapVo.getRemark(), grapVo.getIsPublish(), grapVo.getNick(), openId, grapVo.getAvatar());
        if (null == id) {
            vo.setMsg("fail");
        }
        return vo;
    }

    @GetMapping(value = "/{openId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "抢包任务的合计值查询", notes = "抢包任务的合计值查询<br><br><b>@author zhen.pan</b>")
    public GrapRedPackageCountVO publish(@ApiParam(value = "openId", required = true) @PathVariable String openId) {
        return grapRedPackageTaskService.countPublish(openId);
    }

    @GetMapping(value = "/{openId}/{pageNumber}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "已抢包列表查询", notes = "已抢包列表查询<br><br><b>@author zhen.pan</b>")
    public PageData<GrapRedPackageTaskVO> order(@ApiParam(value = "openId", required = true) @PathVariable String openId,
                                                @ApiParam(value = "pageNumber", required = true) @PathVariable Integer pageNumber,
                                                @ApiParam(value = "pageSize", required = true) @PathVariable Integer pageSize) {
        return grapRedPackageTaskService.queryGrapTask(openId, pageNumber, pageSize);
    }
}
