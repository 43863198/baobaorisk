package com.aizhixin.baobaorisk.redpackage.entity;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.aizhixin.baobaorisk.redpackage.core.RedPackageTaskStatus;
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
@ApiModel(description="红包任务")
@Entity(name = "T_RED_TASK")
@NoArgsConstructor
@ToString
public class RedTask implements java.io.Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "openId")
    @Column(name = "OPENID")
    @Getter @Setter private String openId;

    @ApiModelProperty(value = "订单号")
    @Column(name = "TRADE_NO")
    @Getter @Setter private String tradeNo;

    @ApiModelProperty(value = "红包任务名称")
    @Column(name = "TASK_NAME")
    @Getter @Setter private String taskName;

    @ApiModelProperty(value = "红包金额(分)")
    @Column(name = "TOTAL_FEE")
    @Getter @Setter private Integer totalFee;

    @ApiModelProperty(value = "红包数量")
    @Column(name = "NUM")
    @Getter @Setter private Integer num;

    @ApiModelProperty(value = "红包状态，任务创建10，任务中20，任务完成80")
    @Column(name = "RED_STATUS")
    @Getter @Setter private int redStatus = RedPackageTaskStatus.CREATED.getStateCode();

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
