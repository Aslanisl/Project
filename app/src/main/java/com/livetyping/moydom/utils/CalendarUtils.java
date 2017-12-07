package com.livetyping.moydom.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ivan on 07.12.2017.
 */

public class CalendarUtils {
    private final static String DATE_TIMESTAMP_FORMAT = "yyyy-MM-dd";
    private final static String DATE_SERVER_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDate(){
        SimpleDateFormat sdfServerFormat = new SimpleDateFormat(DATE_TIMESTAMP_FORMAT, Locale.getDefault());
        return sdfServerFormat.format(Calendar.getInstance().getTime());
    }

    public static int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static long getTimeMillisFromServerDate(String date){
        SimpleDateFormat sdfServerFormat = new SimpleDateFormat(DATE_SERVER_TIMESTAMP_FORMAT, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdfServerFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }

    public static boolean isTodayDate(String date){
        SimpleDateFormat sdfServerFormat = new SimpleDateFormat(DATE_TIMESTAMP_FORMAT, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        return date.contains(sdfServerFormat.format(c.getTime()));
    }
}
