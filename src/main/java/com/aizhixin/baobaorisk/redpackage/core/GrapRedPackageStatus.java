package com.aizhixin.baobaorisk.redpackage.core;

import lombok.Getter;

/**
 * 抢红包任务状态
 * 待审核10，审核通过20，审核未通过30，任务失效80
 */
public enum GrapRedPackageStatus {
    WAITING(10),//待审核10
    PASSED(20),//审核通过20
    NOT_PASSING(30),//审核未通过30
    INVALID(80);//任务失效80

    @Getter private int stateCode;
    @Getter private String stateDesc;

    GrapRedPackageStatus(int statusCode) {
        this.stateCode = statusCode;
        switch (statusCode) {
            case 10: stateDesc = "待审核"; break;
            case 20: stateDesc = "通过"; break;
            case 30: stateDesc = "未通过"; break;
            default:
                this.stateCode = 80;
                stateDesc = "失效";
        }
    }
}
