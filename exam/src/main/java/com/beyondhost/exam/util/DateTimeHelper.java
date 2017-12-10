package com.beyondhost.exam.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {

    public static Date addDays(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +num);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }

    public static String format(Date date,String format)
    {
        SimpleDateFormat timeFormater=new SimpleDateFormat(format);
        return timeFormater.format(date);
    }

}
