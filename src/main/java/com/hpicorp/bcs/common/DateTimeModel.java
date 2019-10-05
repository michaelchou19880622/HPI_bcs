package com.hpicorp.bcs.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateTimeModel {	
	
	private DateTimeModel() {		
	}
	
	public static Date timeZoneTaiwan(Date date) throws ParseException {
		SimpleDateFormat isoFormat = new SimpleDateFormat();
		isoFormat.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
		String format = isoFormat.format(date);
		return isoFormat.parse(format);
	}
	
	public static Date setDate(Date date, Integer hour, Integer minute, Integer second, Integer milesecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, milesecond);
		return calendar.getTime();
	}
	
	public static Date initialDate() throws ParseException {
		return DateTimeModel.timeZoneTaiwan(new Date(0));
	}
	
	public static Date maximumTime(Date date) throws ParseException {
		Date tempDate= DateTimeModel.timeZoneTaiwan(date);
		return setDate(tempDate, 23, 59, 59, 999);
	}
	
	public static Date minimizeTime(Date date) throws ParseException {
		Date tempDate= DateTimeModel.timeZoneTaiwan(date);
		return setDate(tempDate, 0, 0, 0, 0);
	}
	
	public static Date plusMillisecond(Date date, long millis) {
		Calendar timeout = Calendar.getInstance();
		timeout.setTimeInMillis(date.getTime() + millis);
		return timeout.getTime();
	}
	
}
