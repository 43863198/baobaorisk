package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="抢包任务统计数据")
@NoArgsConstructor
@ToString
public class GrapRedPackageCountVO {
    @ApiModelProperty(value = "昵称")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "头像url")
    @Getter @Setter private String avatar;

    @ApiModelProperty(value = "参与任务数量")
    @Getter @Setter private int taskNum = 0;

    @ApiModelProperty(value = "抢包总金额")
    @Getter @Setter private double totalFee = 0.0;

    @ApiModelProperty(value = "抢包成功总数量")
    @Getter @Setter private int redNum = 0;

    public GrapRedPackageCountVO(String nick , String avatar, Long taskNum, Double totalFee, Long redNum) {
        this.nick = nick;
        this.avatar = avatar;
        this.taskNum = null == taskNum? 0 : taskNum.intValue();
        this.totalFee = null == totalFee? 0.0 : totalFee;
        this.redNum = null == redNum? 0 : redNum.intValue();
    }
}
