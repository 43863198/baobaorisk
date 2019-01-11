package com.aizhixin.baobaorisk.redpackage.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ApiModel(description="发布的红包任务")
@NoArgsConstructor
@ToString
public class PublishRedPackageVO {

    @ApiModelProperty(value = "任务ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "红包总金额")
    @Getter @Setter private double totalFee = 0.0;

    @ApiModelProperty(value = "红包数量")
    @Getter @Setter private int num = 0;

    @ApiModelProperty(value = "完成数量")
    @Getter @Setter private int completeNum = 0;

    @ApiModelProperty(value = "任务描述")
    @Getter @Setter private String remark;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @Getter @Setter private Date createDate;

    @ApiModelProperty(value = "红包状态，任务创建(未支付完成)10，任务启动20，任务完成80")
    @Getter @Setter private int status;

    public PublishRedPackageVO (String id, String remark, Double totalFee, Integer num, Date createDate, Integer status) {
        this.id = id;
        this.remark = remark;
        this.totalFee = totalFee;
        this.num = num;
        this.createDate = createDate;
        this.status = status;
    }
}
