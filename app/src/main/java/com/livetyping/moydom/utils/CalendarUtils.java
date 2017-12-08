package com.livetyping.moydom.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ivan on 07.12.2017.
 */

public class CalendarUtils {
    private final static String DATE_TIMESTAMP_FORMAT = "yyyy-MM-dd";
    private final static String DATE_SERVER_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final static String DATE_CURRENT_MONTH = "MMMM, yyyy";
    private final static String DATE_FORMAT_DD_MMM = "dd MMMM";

    public static String getCurrentDate(){
        SimpleDateFormat sdfServerFormat = new SimpleDateFormat(DATE_TIMESTAMP_FORMAT, Locale.getDefault());
        return sdfServerFormat.format(Calendar.getInstance().getTime());
    }

    public static int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static String getCurrentMonthText(){
        SimpleDateFormat sdfServerFormat = new SimpleDateFormat(DATE_CURRENT_MONTH, Locale.getDefault());
        return sdfServerFormat.format(Calendar.getInstance().getTime());
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

    public static String getBetweenDateFromTimeMillis(long startTimeMillis, long finishTimeMillis){
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTimeMillis);
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTimeInMillis(finishTimeMillis);
        SimpleDateFormat sdfServerFormat = new SimpleDateFormat(DATE_FORMAT_DD_MMM, Locale.getDefault());
        if (finishTimeMillis == 0){
            Date date = new Date(startCalendar.getTimeInMillis());
            return sdfServerFormat.format(date);
        }
        if (startCalendar.get(Calendar.DAY_OF_MONTH) == finishCalendar.get(Calendar.DAY_OF_MONTH)
                && startCalendar.get(Calendar.MONTH) == finishCalendar.get(Calendar.MONTH)
                && startCalendar.get(Calendar.YEAR) == finishCalendar.get(Calendar.YEAR)){
            return String.valueOf(startCalendar.get(Calendar.DAY_OF_MONTH))
                    + " " + startCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                    + " " + startCalendar.get(Calendar.YEAR);
        }
        int startMonth = startCalendar.get(Calendar.MONTH);
        int finishMonth = finishCalendar.get(Calendar.MONTH);
        if (startMonth == finishMonth){
            return String.valueOf(startCalendar.get(Calendar.DAY_OF_MONTH))
                    + " - " + String.valueOf(finishCalendar.get(Calendar.DAY_OF_MONTH))
                    + " " + startCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                    + " " + startCalendar.get(Calendar.YEAR);
        }
        return String.valueOf(startCalendar.get(Calendar.DAY_OF_MONTH))
                + " " + startCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                + " - " + String.valueOf(finishCalendar.get(Calendar.DAY_OF_MONTH))
                + " " + finishCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                + " " + finishCalendar.get(Calendar.YEAR);
    }
}
