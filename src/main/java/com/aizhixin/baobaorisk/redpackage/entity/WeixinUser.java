package com.aizhixin.baobaorisk.redpackage.entity;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@ApiModel(description="微信用户及红包金额统计信息")
@Entity(name = "T_WEIXIN_USER")
@NoArgsConstructor
@ToString
public class WeixinUser implements java.io.Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "openId")
    @Column(name = "OPEN_ID")
    @Getter @Setter private String openId;

    @ApiModelProperty(value = "微信头像URL")
    @Column(name = "AVATAR")
    @Getter @Setter private String avatar;

    @ApiModelProperty(value = "昵称")
    @Column(name = "NICK")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "发布任务数")
    @Column(name = "PUBLISH_TASK_NUMS")
    @Getter @Setter private Long publishTaskNums;

    @ApiModelProperty(value = "发布任务总金额")
    @Column(name = "PUBLISH_TOTAL_FEE")
    @Getter @Setter private Long publishTotalFee;

    @ApiModelProperty(value = "发布红包数")
    @Column(name = "PUBLISH_RED_NUMS")
    @Getter @Setter private Long publishRedNums;

    @ApiModelProperty(value = "抢包参与任务数")
    @Column(name = "GRAP_TASK_NUMS")
    @Getter @Setter private Long grapTaskNums;

    @ApiModelProperty(value = "抢包任务总金额")
    @Column(name = "GRAP_TOTAL_FEE")
    @Getter @Setter private Long grapTotalFee;

    @ApiModelProperty(value = "抢包成功数")
    @Column(name = "GRAP_RED_NUMS")
    @Getter @Setter private Long grapRedNums;

    @ApiModelProperty(value = "用户状态，预留，缺省值：10")
    @Column(name = "USER_STATUS")
    @Getter @Setter private Integer userStatus = 10;

    @ApiModelProperty(value = "创建时间")
    @CreatedDate
    @Column(name = "CREATED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdDate;

    @ApiModelProperty(value = "最后一次修改时间")
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date lastModifiedDate;

    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter private Integer deleteFlag = DataValidity.VALID.getState();
}
