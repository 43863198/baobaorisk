package com.aizhixin.baobaorisk.redpackage.core;

import lombok.Getter;

/**
 * 交易类型
 * 微信发包10,微信抢包20,微信提现30
 */
public enum TradeType {
    WX_PAY(10),//微信发包10
    RED(20),//微信抢包20
    WX_WITHDRAW(30);//微信提现30

    @Getter private int stateCode;
    @Getter private String stateDesc;

    TradeType(int statusCode) {
        this.stateCode = statusCode;
        switch (statusCode) {
            case 10: stateDesc = "微信发包"; break;
            case 20: stateDesc = "微信抢包"; break;
            default:
                this.stateCode = 30;
                stateDesc = "微信提现";
        }
    }
}
