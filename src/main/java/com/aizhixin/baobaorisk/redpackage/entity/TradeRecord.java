package com.aizhixin.baobaorisk.redpackage.entity;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.aizhixin.baobaorisk.redpackage.core.TradeType;
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
@ApiModel(description="交易记录")
@Entity(name = "T_TRADE_RECORD")
@NoArgsConstructor
@ToString
public class TradeRecord implements java.io.Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "openId")
    @Column(name = "OPENID")
    @Getter @Setter private String openId;

    @ApiModelProperty(value = "订单号或者任务号")
    @Column(name = "TRADE_NO")
    @Getter @Setter private String tradeNo;

    @ApiModelProperty(value = "交易描述")
    @Column(name = "TRADE_NAME")
    @Getter @Setter private String tradeName;

    @ApiModelProperty(value = "微信订单号")
    @Column(name = "TRANSACTION_ID")
    @Getter @Setter private String transactionId;

    @ApiModelProperty(value = "订单金额(分)")
    @Column(name = "TOTAL_FEE")
    @Getter @Setter private Integer totalFee;

    @ApiModelProperty(value = "现金支付(提现、红包)金额(分)")
    @Column(name = "CASH_FEE")
    @Getter @Setter private Integer cashFee;

    @ApiModelProperty(value = "提现手续费(分)")
    @Column(name = "DISCOUNT_FEE")
    @Getter @Setter private Integer discountFee;

    @ApiModelProperty(value = "付款银行")
    @Column(name = "BANK_TYPE")
    @Getter @Setter private String bankType;

    @ApiModelProperty(value = "交易完成时间")
    @Column(name = "TIME_END")
    @Getter @Setter private String timeEnd;

    @ApiModelProperty(value = "交易类型:微信发包10,微信抢包20,微信提现30")
    @Column(name = "TRADE_TYPE")
    @Getter @Setter private int tradeType = TradeType.WX_PAY.getStateCode();

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
