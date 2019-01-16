package com.aizhixin.baobaorisk.redpackage.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ApiModel(description="抢红包任务列表")
@NoArgsConstructor
@ToString
public class GrapRedPackageListVO {

    @ApiModelProperty(value = "红包任务ID")
    @Getter @Setter private String taskId;

    @ApiModelProperty(value = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "七牛图片Key")
    @Getter @Setter private String picName;

    @ApiModelProperty(value = "文字描述")
    @Getter @Setter private String remark;

    @ApiModelProperty(value = "是否向所有参与者公开:公开10，不公开20")
    @Getter @Setter private Integer picPublish;

    @ApiModelProperty(value = "抢红包人的昵称")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "抢红包人的微信头像")
    @Getter @Setter private String avatar;

    @ApiModelProperty(value = "抢红包任务所获得的红包金额")
    @Getter @Setter private Double totalFee;

    @ApiModelProperty(value = "审核日期")
    @JsonFormat(pattern = "MM-dd HH:mm", timezone = "GMT+8")
    @Getter @Setter private Date verifyDate;

    @ApiModelProperty(value = "抢红包任务状态，待审核10，审核通过20，审核未通过30，任务失效80")
    @Getter @Setter private int taskStatus;

    public GrapRedPackageListVO(String taskId, String id, String picName, String remark, Integer picPublish, String nick, String avatar, Integer totalFee, Date verifyDate, Integer taskStatus) {
        this.taskId = taskId;
        this.id = id;
        this.picName = picName;
        this.remark = remark;
        this.picPublish = picPublish;
        this.nick = nick;
        this.avatar = avatar;
        this.totalFee = (null != totalFee ? totalFee / 100.0 : null);
        this.verifyDate = verifyDate;
        this.taskStatus = taskStatus;
    }
}
