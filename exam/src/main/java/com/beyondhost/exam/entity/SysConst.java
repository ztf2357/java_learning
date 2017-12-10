package com.beyondhost.exam.entity;

import java.util.Calendar;
import java.util.Date;

public class SysConst {

    public static String CRAWL_JOB_CACHE_BLOCK_NAME  = "CrawlJobCacheBlock";

    public static String CRAWL_JOB_CACHE_KEY ="CurrentJobKey";

    public static Date DEFAULT_DATE(){
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH, 31);  //设置日期
        instance.set(Calendar.MONTH, 12-1 );
        instance.set(Calendar.YEAR, 9999);
        instance.set(Calendar.HOUR, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        return instance.getTime();
    }


    public static Date TODAY_ZERO_TIME() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
