package com.skytech.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Zhangyk
 * Date: 14-5-26
 * Time: 下午7:43
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils {
    private static Calendar calendar = Calendar.getInstance();

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static String formatDate(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.format(dateFormat.parse(time));
        } catch (ParseException e) {
            return "";
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String preciseMinute(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return dateFormat.format(dateFormat.parse(time));
        } catch (ParseException e) {
            return "";
        }
    }

    public static String formatDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static String getDayOfMonthString(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(dateFormat.parse(date));
        return "" + calendar.get(Calendar.DAY_OF_MONTH);
    }
}
