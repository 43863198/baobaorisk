package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="微信用户OPENID的JSON封装")
@NoArgsConstructor
@ToString
public class WxConfirmVO {
    @ApiModelProperty(value = "return_code")
    @Getter @Setter private String return_code = "SUCCESS";

    @ApiModelProperty(value = "return_msg")
    @Getter @Setter private String return_msg;
}
