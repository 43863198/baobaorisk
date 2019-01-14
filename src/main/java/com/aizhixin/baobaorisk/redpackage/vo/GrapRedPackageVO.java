package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="抢红包任务")
@NoArgsConstructor
@ToString
public class GrapRedPackageVO {

    @ApiModelProperty(value = "红包任务ID")
    @Getter @Setter private String taskId;

    @ApiModelProperty(value = "七牛图片Key")
    @Getter @Setter private String picName;

    @ApiModelProperty(value = "文字描述")
    @Getter @Setter private String remark;

    @ApiModelProperty(value = "是否向所有参与者公开:公开10，不公开20")
    @Getter @Setter private Integer isPublish;

    @ApiModelProperty(value = "抢红包人的昵称")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "抢红包人的微信头像")
    @Getter @Setter private String avatar;
}
