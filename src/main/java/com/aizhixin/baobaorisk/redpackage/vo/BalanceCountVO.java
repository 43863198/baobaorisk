package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="余额统计数据")
@NoArgsConstructor
@ToString
public class BalanceCountVO {
    @ApiModelProperty(value = "昵称")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "头像url")
    @Getter @Setter private String avatar;

    @ApiModelProperty(value = "抢包总金额")
    @Getter @Setter private double balance = 0.0;

}
