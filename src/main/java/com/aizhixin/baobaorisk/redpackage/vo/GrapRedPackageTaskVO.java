package com.aizhixin.baobaorisk.redpackage.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ApiModel(description="抢红包任务")
@NoArgsConstructor
@ToString
public class GrapRedPackageTaskVO {

    @ApiModelProperty(value = "发包人的昵称")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "发包人的微信头像")
    @Getter @Setter private String avatar;

    @ApiModelProperty(value = "发包文字描述")
    @Getter @Setter private String remark;

    @ApiModelProperty(value = "发包创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @Getter @Setter private Date createDate;

    @ApiModelProperty(value = "红包金额")
    @Getter @Setter private double totalFee;

    @ApiModelProperty(value = "抢包状态:待审核10，审核通过20，审核未通过30，任务失效(完成)80")
    @Getter @Setter private int grapStatus;

    public GrapRedPackageTaskVO (String nick, String avatar, String remark, Date createDate, Integer totalFee, int grapStatus) {
        this.nick = nick;
        this.avatar = avatar;
        this.remark = remark;
        this.createDate = createDate;
        this.totalFee = (null == totalFee ? 0.0 : totalFee / 100.0);
        this.grapStatus = grapStatus;
    }
}
