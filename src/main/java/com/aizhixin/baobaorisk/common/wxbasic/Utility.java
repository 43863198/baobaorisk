package com.aizhixin.baobaorisk.common.wxbasic;

import java.util.UUID;

public class Utility {
    public static String getCurrentTimeStamp() {
        return "" + System.currentTimeMillis()/1000;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
