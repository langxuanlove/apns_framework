package com.kk.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
	public static String getTodayBefore(String interval) {
		String intervalTime = "-" + interval;
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(calendar.DATE, Integer.parseInt(intervalTime)); // 这个时间就是日期往后推一天的结果
		date = calendar.getTime();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	public static String getTodayAfter(String interval) {
		String intervalTime = interval;
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(calendar.DATE, Integer.parseInt(intervalTime)); // 这个时间就是日期往后推一天的结果
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	public static String getBeforSecond(String interval) {
		String intervalTime = "-" + interval;
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(calendar.SECOND, Integer.parseInt(intervalTime)); 
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
	public static void main(String[] args) {
//		System.out.println(CalendarUtil.getTodayBefore("260"));
//		System.out.println(CalendarUtil.getTodayAfter("360"));
		System.out.println(CalendarUtil.getBeforSecond("20"));
	}
}
