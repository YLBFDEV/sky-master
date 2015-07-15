package com.skytech.android.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    private static DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public static String getDayOfWeek(String date) throws ParseException {
        return String.format("%tA", new SimpleDateFormat("yyyy-MM-dd").parse(date));
    }

    public static String getDay(String date) throws ParseException {
        return String.format("%td", new SimpleDateFormat("yyyy-MM-dd").parse(date));
    }

    public static String getHourMinute(String date) throws ParseException {
        Date _date = formatDate.parse(date);
        return String.format(String.format("%tH点", _date)) + String.format(String.format("%tM分", _date));
    }

    public static String getDateAndHourMinute(String date) throws ParseException {
        Date _date = formatDate.parse(date);
        return String.format(formatDate.format(_date));
    }

    public static String getYearAndMonth(String date) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        return String.format("%d年%tm月", c.get(Calendar.YEAR), new SimpleDateFormat("yyyy-MM-dd").parse(date));
    }
}
