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
public class OpenIdVO {
    @ApiModelProperty(value = "openId")
    @Getter @Setter private String openId = "0";
}
