package com.madm.learnroute.constant;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @author dongming.ma
 * @date 2022/7/3 20:53
 */
public interface TimeUnitTable {
    long allDayOfYear = LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear()).getDayOfYear();
    long MILLISECOND = 1l;
    long SECOND = MILLISECOND * 1000;
    long MINUTE = 60 * SECOND;
    long HOUR = 60 * MINUTE;
    long DAY = 24 * HOUR;
    long YEAR = allDayOfYear * DAY;
}
