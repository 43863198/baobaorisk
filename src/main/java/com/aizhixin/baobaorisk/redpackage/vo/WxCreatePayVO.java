package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="微信预支付返回结果")
@NoArgsConstructor
@ToString
public class WxCreatePayVO {
    @ApiModelProperty(value = "红包总金额(大于0，小于200)")
    @Getter @Setter private Double totalFee;

    @ApiModelProperty(value = "红包数量(大于0，小于100)")
    @Getter @Setter private Integer num;

    @ApiModelProperty(value = "任务描述(不超过25个字符)")
    @Getter @Setter private String remark;
}
