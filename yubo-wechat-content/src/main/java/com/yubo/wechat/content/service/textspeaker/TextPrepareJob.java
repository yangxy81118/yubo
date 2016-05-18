package com.yubo.wechat.content.service.textspeaker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.yubo.wechat.content.dao.MessageTextMapper;

/**
 * 文本内容准备定时任务
 * 
 * @author young.jason
 *
 */
@Component
public class TextPrepareJob {

	private static final Logger logger = LoggerFactory
			.getLogger(TextPrepareJob.class);

	private static List<TextScdlEntry> workdaySchedule = new ArrayList<>();

	private static List<TextScdlEntry> weekendSchedule = new ArrayList<>();

	@Autowired
	MessageTextMapper messageTextMapper;

	// 对定时任务的处理进行加载
	static {
		Properties props = null;
		try {
			props = PropertiesLoaderUtils
					.loadAllProperties("app-schedule.properties");

			buildScheduleList(props);
			Collections.sort(workdaySchedule, new TextScheduleSortComparetor());
			Collections.sort(weekendSchedule, new TextScheduleSortComparetor());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@PostConstruct
	public void initJob() {

		// 开始执行定时任务
		int startPeriodId = getCurrentPeriodId();
		Timer timer = new Timer();
		TextHunter firstHunter = new TextHunter(startPeriodId,messageTextMapper);
		timer.schedule(firstHunter, 0);

	}

	/**
	 * 根据当前系统的时间获取当前应该执行的PeriodId
	 * 
	 * @return
	 */
	private int getCurrentPeriodId() {

		long nowTime = System.currentTimeMillis();

		int currentPeriodId = 0;

		for (int i = 0; i < workdaySchedule.size(); i++) {
			TextScdlEntry entry = workdaySchedule.get(i);
			if (nowTime >= entry.startTime && nowTime < entry.endTime) {
				currentPeriodId = entry.periodId;
			}
		}

		return currentPeriodId;
	}

	private static void buildScheduleList(Properties props) {
		Set<Entry<Object, Object>> propElements = props.entrySet();
		for (Entry<Object, Object> entry : propElements) {
			String key = entry.getKey().toString();

			if (key.startsWith(KEY_SCHEDULE_PREFIX)) {
				TextScdlEntry dayEntry = new TextScdlEntry();
				String[] ktemp = key.split("\\.");
				dayEntry.periodId = Integer.parseInt(ktemp[ktemp.length - 1]);
				String values[] = entry.getValue().toString().split(",");
				dayEntry.startTime = parseTime(values[0]);
				dayEntry.endTime = parseTime(values[1]);
				if (key.startsWith(KEY_SCHEDULE_WORKDAY_PREFIX)) {
					workdaySchedule.add(dayEntry);
				} else if (key.startsWith(KEY_SCHEDULE_WEEKEND_PREFIX)) {
					weekendSchedule.add(dayEntry);
				}
			}

		}

	}

	/**
	 * 将配置中的小时换算成一个具体的时间
	 * 
	 * @param string
	 * @return
	 */
	private static long parseTime(String hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		return cal.getTimeInMillis();
	}
	
	private static final String KEY_SCHEDULE_WEEKEND_PREFIX = "textspeaker.schedule.weekend.";
	private static final String KEY_SCHEDULE_WORKDAY_PREFIX = "textspeaker.schedule.workday.";
	private static final String KEY_SCHEDULE_PREFIX = "textspeaker.schedule.";

	public static List<TextScdlEntry> getWorkdaySchedule() {
		return workdaySchedule;
	}

	public static List<TextScdlEntry> getWeekendSchedule() {
		return weekendSchedule;
	}

}
