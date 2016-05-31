package com.yubo.wechat.support;

import java.util.Calendar;
import java.util.TimeZone;

public class TimeUtil {

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
