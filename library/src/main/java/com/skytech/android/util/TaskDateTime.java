package com.skytech.android.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressLint("SimpleDateFormat")
public class TaskDateTime {
	private Date date;
	private TaskDateTime today;

	public TaskDateTime() {
		this.date = new Date();
	}

	public TaskDateTime(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return this.date;
	}

	public TaskDateTime(String strDate) {
		SimpleDateFormat format_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			this.date = format_yyyy_MM_dd_HH_mm_ss.parse(strDate);
		} catch (ParseException e) {
			Log.e("ParseException", e.getMessage());
			this.date = new Date();
		}
	}

	public Date getRawDate() {
		return this.date;
	}

	public TaskDateTime getToday() {
		if (today == null) {
			today = new TaskDateTime();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(today.getTime());
			// 此处好像是去除0
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			today = new TaskDateTime(calendar.getTime());
		}
		return today;
	}

	public TaskDateTime getTomorrow() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getToday().getTime());
		calendar.add(GregorianCalendar.DATE, 1);
		return new TaskDateTime(calendar.getTime());
	}

	public Date getYesterday() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getToday().getTime());
		calendar.add(GregorianCalendar.DATE, -1);
		return calendar.getTime();
	}

	public Date getMonday() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getToday().getTime());
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar.getTime();
	}

	public Date getMonth() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getToday().getTime());
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar.getTime();
	}

	public Date getBeforeMonthLastDay() throws ParseException {
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String time = format.format(getToday()) + "-01";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date month = dateFormat.parse(time);
		calendar.setTime(month);
		// 最每月最后一天
		calendar.add(GregorianCalendar.DATE, -1);
		return calendar.getTime();
	}

	public Date getBeforeSecondMonthLastDay() throws ParseException {
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String time = format.format(getToday()) + "-01";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date month = dateFormat.parse(time);
		calendar.setTime(month);
		// 最每月最后一天
		calendar.add(GregorianCalendar.MONTH, -1);
		calendar.add(GregorianCalendar.DATE, -1);
		return calendar.getTime();
	}

	public String getChinaDate() throws ParseException {
		// TODO Auto-generated method stub
		if (this.date.getDate() == getToday().getTime().getDate()) {
			return "今天";
		} else if (this.date.getDate() == getYesterday().getDate()) {
			return "昨天";
		} else if (this.date.after(getMonday())) {
			return "本周";
		} else if (this.date.after(getBeforeMonthLastDay())) {
			return "本月";
		} else if (this.date.after(getBeforeSecondMonthLastDay())) {
			return "上月";
		} else {
			SimpleDateFormat dformat = new SimpleDateFormat("yyyy");
			return dformat.format(this.date);
		}
	}

	public String getMonthDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
		return sdf.format(this.date);
	}

	public String getHoursMinute() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		return sdf.format(this.date);
	}

	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(this.date);
	}

	public String toString(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(this.date);
	}
}
