package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="发布的红包任务统计数据")
@NoArgsConstructor
@ToString
public class PublishRedPackageCountVO {

    @ApiModelProperty(value = "任务数量")
    @Getter @Setter private int taskNum = 0;

    @ApiModelProperty(value = "红包总金额")
    @Getter @Setter private double totalFee = 0.0;

    @ApiModelProperty(value = "红包总数量")
    @Getter @Setter private int redNum = 0;

    public PublishRedPackageCountVO(Long taskNum, Double totalFee, Long redNum) {
        this.taskNum = null == taskNum? 0 : taskNum.intValue();
        this.totalFee = null == totalFee? 0.0 : totalFee;
        this.redNum = null == redNum? 0 : redNum.intValue();
    }
}
