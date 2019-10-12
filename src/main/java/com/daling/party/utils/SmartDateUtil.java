package com.daling.party.utils;

import java.util.Calendar;
import java.util.Date;

public class SmartDateUtil {
    public static Date getAddDayBeginTime(int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, days);
        return getDayOfBegTime(cal);
    }
    public static Date getAddDayBeginTime(int days,Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return getDayOfBegTime(cal);
    }

    public static Date getDayOfBegTime(Calendar cal){
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
