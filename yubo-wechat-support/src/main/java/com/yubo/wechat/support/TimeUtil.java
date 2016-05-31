package com.yubo.wechat.support;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

	
	/**
	 * 格式化时间,只输出日
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date){
		return formatTime(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatTime(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	
	/**
	 * 获取北京时间Calendar对象
	 * 
	 * @return
	 */
	public static Calendar getChinaCalendar() {
		return getChinaCalendar(null,null,null,null);
	}
	
	
	/**
	 * 获取北京时间Calendar对象
	 * 
	 * @return
	 */
	public static Calendar getChinaCalendar(Integer hourOfDay, Integer minute, Integer second) {
		return getChinaCalendar(null,hourOfDay,minute,second);
	}
	
	
	/**
	 * 获取北京时间Calendar对象
	 * 
	 * @return
	 */
	public static Calendar getChinaCalendar(Integer dateOffset,Integer hourOfDay, Integer minute, Integer second) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		if(hourOfDay!=null){
			cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		}
		
		if(minute!=null){
			cal.set(Calendar.MINUTE, minute);
		}
		
		if(second!=null){
			cal.set(Calendar.SECOND, second);
		}
		
		if(dateOffset!=null){
			cal.add(Calendar.DATE, dateOffset);
		}
		
		return cal;
	}
}
