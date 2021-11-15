package com.bjfu.exam.core.util;

import java.util.Date;

public class DateUtil {
    /**
     * 计算出起始到截止 间隔的秒数字
     */
    public static Integer calLastedTime(Date startDate, Date endDate) {
        long a = endDate.getTime();
        long b = startDate.getTime();
        return (int) ((a - b) / 1000);
    }
}
