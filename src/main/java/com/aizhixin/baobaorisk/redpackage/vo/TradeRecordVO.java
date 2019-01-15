package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="交易记录")
@NoArgsConstructor
@ToString
public class TradeRecordVO {
    @ApiModelProperty(value = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "交易描述")
    @Getter @Setter private String tradeName;

    @ApiModelProperty(value = "现金支付(提现、红包)金额(分)")
    @Getter @Setter private Integer cashFee;

    @ApiModelProperty(value = "提现手续费(分)")
    @Getter @Setter private Integer discountFee;

    @ApiModelProperty(value = "交易完成时间")
    @Getter @Setter private String timeEnd;

    @ApiModelProperty(value = "交易类型:微信发包10,微信抢包20,微信提现30")
    @Getter @Setter private int tradeType;

    public TradeRecordVO(String id, String tradeName, Integer cashFee, Integer discountFee, String timeEnd, int tradeType) {
        this.id = id;
        this.tradeName = tradeName;
        this.cashFee = cashFee;
        this.discountFee = discountFee;
        this.timeEnd = timeEnd;
        this.tradeType = tradeType;
    }
}
