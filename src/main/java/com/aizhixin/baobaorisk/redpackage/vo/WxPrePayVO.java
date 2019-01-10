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
public class WxPrePayVO {
    @ApiModelProperty(value = "预支付是否成功,成功:SUCCESS")
    @Getter @Setter private String return_code = "SUCCESS";

//    @ApiModelProperty(value = "预支付ID")
//    @Getter @Setter private String prepayId;

    @ApiModelProperty(value = "随机字符串")
    @Getter @Setter private String nonceStr;

    @ApiModelProperty(value = "APPID")
    @Getter @Setter private String appId;

//    @ApiModelProperty(value = "MCHID")
//    @Getter @Setter private String mchId;

    @ApiModelProperty(value = "时间戳")
    @Getter @Setter private String timestamp;

    @ApiModelProperty(value = "支付包")
    @Getter @Setter private String payPackage;

    @ApiModelProperty(value = "签名类型")
    @Getter @Setter private String signType;

    @ApiModelProperty(value = "签名")
    @Getter @Setter private String sign;
}
