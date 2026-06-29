package com.sfccn.pdftool.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

	public static final String PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static final String PATTERN_yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_yyyy_MM_dd = "yyyy-MM-dd";
	public static final int DATE = Calendar.DATE;
	public static final int YEAR = Calendar.YEAR;
	public static final int MONTH = Calendar.MONTH;
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String dateToStr(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 *
	 * @param localDateTime
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(LocalDateTime localDateTime, String pattern) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		String data = localDateTime.format(format);
		return data;
	}
	/**
	 *
	 * @param localDateTime
	 * @return
	 */
	public static String dateToStr(LocalDateTime localDateTime) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(PATTERN_yyyyMMdd_HHmmss);
		String data = localDateTime.format(format);
		return data;
	}

	public static Date strToDate(String dateStr, String pattern){
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
//		return LocalDateTime.parse(date, format);
		LocalDateTime localDateTime = LocalDateTime.parse(dateStr, format);
		ZoneId zone = ZoneId.of("Asia/Shanghai");
		Instant instant = localDateTime.atZone(zone).toInstant();
		Date date = Date.from(instant);
		return date;

	}
	public static Date strToTimestamp(String dateStr, String pattern){
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
//		return LocalDateTime.parse(date, format);
		LocalDateTime localDateTime = LocalDateTime.parse(dateStr, format);
		ZoneId zone = ZoneId.of("Asia/Shanghai");
		Instant instant = localDateTime.atZone(zone).toInstant();
		Date date = Date.from(instant);
		return new Timestamp(date.getTime());

	}

	public static LocalDateTime strToLocalDateTime(String date, String pattern){
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(date, format);
	}

	/**
	 * 
	 * @param field Calendar.DATE, Calendar.YEAR, Calendar.MONTH
	 * @param amount
	 * @return
	 */
	public Date add(int field, int amount){
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
	    calendar.add(field, amount);    //得到前一天
	    return calendar.getTime();
	}
	public static String getNowStr(){
//		LocalDateTime arrivalDate = LocalDateTime.now();
//		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		String data = arrivalDate.format(format);
//		return data;
		return getNowStr("yyyy-MM-dd HH:mm:ss");
	}
	public static String getNowStr(String format){
		LocalDateTime arrivalDate = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		String data = arrivalDate.format(dateTimeFormatter);
		return data;
	}
	/**
	 * 发布时间转为时间Date
	 * @param dateStr
	 * @return
	 */
	public static Date parsePublishtime(String dateStr){
		String regEx1 = "[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}[:][0-9]{1,2}";
		Pattern pattern = Pattern.compile(regEx1);
		Matcher matcher = pattern.matcher(dateStr);
		LocalDateTime localDateTime = LocalDateTime.now();
		if(matcher.matches()){
			DateTimeFormatter format = DateTimeFormatter.ofPattern(PATTERN_yyyyMMdd_HHmmss);
			localDateTime = LocalDateTime.parse(dateStr, format);

		}
		ZoneId zone = ZoneId.of("Asia/Shanghai");
		Instant instant = localDateTime.atZone(zone).toInstant();
		Date date = Date.from(instant);
		return date;
	}

	/**
	 * 发布时间转为时间Date
	 * @param localDateTime
	 * @return
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime){
		ZoneId zone = ZoneId.of("Asia/Shanghai");
		Instant instant = localDateTime.atZone(zone).toInstant();
		Date date = Date.from(instant);
		return date;
	}
	public static LocalDateTime DateToLocalDateTime(Date date){
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.of("Asia/Shanghai");// ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		return localDateTime;
	}

	/**
	 * 获取字符串日期格式
	 * @param dayCOunt
	 * @return  yyyy-MM-dd
	 */
	public static String getDayStr(int dayCOunt){
		LocalDateTime arrivalDate = LocalDateTime.now();
		arrivalDate = arrivalDate.plusDays(dayCOunt);
		DateTimeFormatter format = DateTimeFormatter.ofPattern(PATTERN_yyyy_MM_dd);
		String data = arrivalDate.format(format);
		return data;
	}

	/**
	 * 获取秒数
	 * @param dateTime
	 * @return
	 */
	public static long getScenod(LocalDateTime dateTime){
		long second = dateTime.toEpochSecond(ZoneOffset.of("+8"));
		return second;
	}

	/**
	 * 获取毫秒数
	 * @param dateTime
	 * @return
	 */
	public static long getMillisecond(LocalDateTime dateTime){
		long milliSecond = dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
		return milliSecond;
	}

	/**
	 * 毫秒数转日期
	 */
	public static LocalDateTime secondToLocalDateTime(long second){
		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(second,0, ZoneOffset.ofHours(8));
		return localDateTime;
	}


}
