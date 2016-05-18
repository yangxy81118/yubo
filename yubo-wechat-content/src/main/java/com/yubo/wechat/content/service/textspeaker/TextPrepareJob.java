package com.yubo.wechat.content.service.textspeaker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 文本内容准备定时任务
 * 
 * @author young.jason
 *
 */
public class TextPrepareJob {

	private static ScheduledExecutorService textSchedulePool = Executors
			.newScheduledThreadPool(10);

	private static final Logger logger = LoggerFactory
			.getLogger(TextPrepareJob.class);

	private static List<TextScdlEntry> workdaySchedule = new ArrayList<>();
	
	private static List<TextScdlEntry> weekendSchedule = new ArrayList<>();

	//对定时任务的处理进行加载
	static {
		Properties props;
		try {
			props = PropertiesLoaderUtils
					.loadAllProperties("app-schedule.properties");

			buildScheduleList(props);
			Collections.sort(workdaySchedule,new TextScheduleSortComparetor());
			Collections.sort(weekendSchedule,new TextScheduleSortComparetor());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@PostConstruct
	public void initJob() {

		//开始执行定时任务
		
	}

	private static void buildScheduleList(Properties props) {
		Set<Entry<Object, Object>> propElements = props.entrySet();
		for (Entry<Object, Object> entry : propElements) {
			String key = entry.getKey().toString();
			
			if (key.startsWith(KEY_SCHEDULE_PREFIX)) {
				TextScdlEntry dayEntry = new TextScdlEntry();
				String[] ktemp = key.split(".");
				dayEntry.index = Integer.parseInt(ktemp[ktemp.length - 1]);
				String values[] = entry.getValue().toString().split(",");
				dayEntry.startTime = parseTime(values[0]);
				dayEntry.endTime = parseTime(values[0]);
				if (key.startsWith(KEY_SCHEDULE_WORKDAY_PREFIX)) {
					workdaySchedule.add(dayEntry);
				}else if(key.startsWith(KEY_SCHEDULE_WEEKEND_PREFIX)){
					weekendSchedule.add(dayEntry);
				}
			}
			
		}

	}

	/**
	 * 将配置中的小时换算成一个具体的时间
	 * @param string
	 * @return
	 */
	private static long parseTime(String hour) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis();
	}

	private static final String KEY_SCHEDULE_WEEKEND_PREFIX = "textspeaker.schedule.weekend.";
	private static final String KEY_SCHEDULE_WORKDAY_PREFIX = "textspeaker.schedule.workday.";
	private static final String KEY_SCHEDULE_PREFIX = "textspeaker.schedule.";
	public static ScheduledExecutorService getTextSchedulePool() {
		return textSchedulePool;
	}

	public static List<TextScdlEntry> getWorkdaySchedule() {
		return workdaySchedule;
	}

	public static List<TextScdlEntry> getWeekendSchedule() {
		return weekendSchedule;
	}
	
	
	
}
