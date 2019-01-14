package com.aizhixin.baobaorisk.redpackage.entity;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.aizhixin.baobaorisk.redpackage.core.GrapRedPackageStatus;
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
@ApiModel(description="抢红包任务")
@Entity(name = "T_GRAP_RED_TASK")
@NoArgsConstructor
@ToString
public class GrapRedTask implements java.io.Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "红包任务ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    @Getter @Setter private RedTask redTask;

    @ApiModelProperty(value = "抢红包任务人的openid")
    @Column(name = "OPENID")
    @Getter @Setter private String openId;

    @ApiModelProperty(value = "抢红包任务人的昵称")
    @Column(name = "NICK")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "抢红包任务人的头像")
    @Column(name = "AVATAR")
    @Getter @Setter private String avatar;

    @ApiModelProperty(value = "抢红包任务图片路径或者key")
    @Column(name = "PIC")
    @Getter @Setter private String pic;

    @ApiModelProperty(value = "抢红包任务图片是否向所有参与者公开:公开10，不公开20")
    @Column(name = "PIC_PUBLISH")
    @Getter @Setter private Integer picPublish;

    @ApiModelProperty(value = "抢红包任务文字内容")
    @Column(name = "REMARK")
    @Getter @Setter private String remark;

    @ApiModelProperty(value = "抢红包任务所获得的红包金额(分)")
    @Column(name = "TOTAL_FEE")
    @Getter @Setter private Integer totalFee;

    @ApiModelProperty(value = "审核日期")
    @CreatedDate
    @Column(name = "VERIFY_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date verifyDate;

    @ApiModelProperty(value = "抢红包任务状态，待审核10，审核通过20，审核未通过30，任务失效80")
    @Column(name = "TASK_STATUS")
    @Getter @Setter private int taskStatus = GrapRedPackageStatus.WAITING.getStateCode();

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
