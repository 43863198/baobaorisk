package com.aizhixin.baobaorisk.redpackage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="已抢红包数量及金额统计")
@NoArgsConstructor
@ToString
public class RedPackageCountDTO {
    @ApiModelProperty(value = "参与人数")
    @Getter @Setter private int countNums;

    @ApiModelProperty(value = "审核人数")
    @Getter @Setter private int verifyNums;

    @ApiModelProperty(value = "已抢红包数量")
    @Getter @Setter private int grapNums;

    @ApiModelProperty(value = "已抢红包的金额")
    @Getter @Setter private int  totalFee;

    public RedPackageCountDTO(Long countNums, Long verifyNums, Long grapNums, Long totalFee) {
        this.countNums = (null == countNums ? 0 : countNums.intValue());
        this.verifyNums = (null == verifyNums ? 0 : verifyNums.intValue());
        this.grapNums = (null == grapNums ? 0 : grapNums.intValue());
        this.totalFee = (null == totalFee ? 0 : totalFee.intValue());
    }
}
