package com.like4u.papermanager.common.utlis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/26 14:54
 */
public class TimeUtils {

    public static String nowTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(now);
    }
}
