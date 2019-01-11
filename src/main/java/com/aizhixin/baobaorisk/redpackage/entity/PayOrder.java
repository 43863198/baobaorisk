package com.aizhixin.baobaorisk.redpackage.entity;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.aizhixin.baobaorisk.redpackage.core.TradeStatus;
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
@ApiModel(description="交易记录/支付订单")
@Entity(name = "T_PAY_ORDER")
@NoArgsConstructor
@ToString
public class PayOrder implements java.io.Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "openId")
    @Column(name = "OPENID")
    @Getter @Setter private String openId;

    @ApiModelProperty(value = "昵称")
    @Column(name = "NICK")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "头像")
    @Column(name = "AVATAR")
    @Getter @Setter private String avatar;

    @ApiModelProperty(value = "订单号/交易号")
    @Column(name = "TRADE_NO")
    @Getter @Setter private String tradeNo;

    @ApiModelProperty(value = "交易名称")
    @Column(name = "TRADE_NAME")
    @Getter @Setter private String tradeName;

    @ApiModelProperty(value = "任务名称")
    @Column(name = "TASK_NAME")
    @Getter @Setter private String taskName;

    @ApiModelProperty(value = "交易/订单金额(分)")
    @Column(name = "TOTAL_FEE")
    @Getter @Setter private Integer totalFee;

    @ApiModelProperty(value = "红包数量")
    @Column(name = "NUM")
    @Getter @Setter private Integer num;

    @ApiModelProperty(value = "预付单号")
    @Column(name = "PREPAY_ID")
    @Getter @Setter private String prepayId;

    @ApiModelProperty(value = "微信支付订单号")
    @Column(name = "TRANSACTION_ID")
    @Getter @Setter private String transactionId;

    @ApiModelProperty(value = "现金支付金额(分)")
    @Column(name = "CASH_FEE")
    @Getter @Setter private Integer cashFee;

    @ApiModelProperty(value = "付款银行")
    @Column(name = "BANK_TYPE")
    @Getter @Setter private String bankType;

    @ApiModelProperty(value = "支付完成时间")
    @Column(name = "TIME_END")
    @Getter @Setter private String timeEnd;

    @ApiModelProperty(value = "交易状态，预支付10，支付失败20，支付成功80")
    @Column(name = "PAY_STATUS")
    @Getter @Setter private int payStatus = TradeStatus.PrePay.getStateCode();

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
