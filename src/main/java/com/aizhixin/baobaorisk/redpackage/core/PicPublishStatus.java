package com.aizhixin.baobaorisk.redpackage.core;

import lombok.Getter;

/**
 * 图片是否给所有参与者公开
 * 公开10，不公开50
 */
public enum PicPublishStatus {
    OPEN(10),//公开10
    CLOSE(80);//不公开50

    @Getter private int stateCode;
    @Getter private String stateDesc;

    PicPublishStatus(int statusCode) {
        this.stateCode = statusCode;
        switch (statusCode) {
            case 10: stateDesc = "公开"; break;
            default:
                this.stateCode = 80;
                stateDesc = "不公开";
        }
    }
}
