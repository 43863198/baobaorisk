package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@ApiModel(description="发布的红包任务")
@NoArgsConstructor
@ToString
public class PublishRedPackageVO {
    @ApiModelProperty(value = "红包总金额")
    @Getter @Setter private Double totalFee = 0.0;

    @ApiModelProperty(value = "红包数量")
    @Getter @Setter private Integer num = 0;

    @ApiModelProperty(value = "完成数量")
    @Getter @Setter private int completeNum = 0;

    @ApiModelProperty(value = "任务描述")
    @Getter @Setter private String remark;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Getter @Setter private String createDate;

    @ApiModelProperty(value = "红包状态，任务创建(未支付完成)10，任务启动20，任务完成80")
    @Getter @Setter private Integer status;
}
