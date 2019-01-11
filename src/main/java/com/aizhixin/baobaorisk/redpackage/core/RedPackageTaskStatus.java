package com.aizhixin.baobaorisk.redpackage.core;

import lombok.Getter;

/**
 * 红包任务状态
 * 任务创建10，任务中20，任务完成80
 */
public enum RedPackageTaskStatus {
    CREATED(10),//任务创建10
    TASKING(20),//任务中20
    COMPELETE(80);//任务完成80

    @Getter private int stateCode;
    @Getter private String stateDesc;

    RedPackageTaskStatus(int statusCode) {
        this.stateCode = statusCode;
        switch (statusCode) {
            case 10: stateDesc = "任务创建"; break;
            case 20: stateDesc = "任务中"; break;
            default:
                this.stateCode = 80;
                stateDesc = "任务完成";
        }
    }
}
