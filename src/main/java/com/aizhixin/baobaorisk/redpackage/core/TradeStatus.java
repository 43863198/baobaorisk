package com.aizhixin.baobaorisk.redpackage.core;

import lombok.Getter;

/**
 * 支付状态
 * 交易状态
 * 交易状态，预支付10，支付失败20，支付成功80
 */
public enum TradeStatus {
    PrePay(10),//预支付
    PrePayFail(20),//支付失败
    PrePaySuccess(80);//支付成功

    @Getter private int stateCode;
    @Getter private String stateDesc;

    TradeStatus(int statusCode) {
        this.stateCode = statusCode;
        switch (statusCode) {
            case 10: stateDesc = "预支付"; break;
            case 20: stateDesc = "支付失败"; break;
            default:
                this.stateCode = 80;
                stateDesc = "支付成功";
        }
    }
}
