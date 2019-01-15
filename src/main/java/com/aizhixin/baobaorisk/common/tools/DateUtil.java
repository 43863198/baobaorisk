package com.aizhixin.baobaorisk.common.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * @return yyyyMMddHHmmss
     */
    public static String formatDate(Date d) {
        return f1.format(d);
    }
}
