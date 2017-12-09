package com.beyondhost.exam.entity;

import java.util.Calendar;
import java.util.Date;

public class SysConst {
    public static Date DEFAULT_DATE(){
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH, 31);  //设置日期
        instance.set(Calendar.MONTH, 12-1 );
        instance.set(Calendar.YEAR, 9999);
        instance.set(Calendar.HOUR, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        return instance.getTime();
    };
}
