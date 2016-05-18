package com.yubo.wechat.content.service.textspeaker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubo.wechat.content.CatsAndDogs;
import com.yubo.wechat.content.dao.MessageTextMapper;
import com.yubo.wechat.content.dao.pojo.MessageText;

/**
 * 去Database加载自身对应的的文本内容<br/>
 * 本结构特点在于链式处理，每个时间段任务执行完后，主动配置下一个时间段任务
 * 
 * @author young.jason
 *
 */
public class TextHunter extends TimerTask {

	private static final Logger logger = LoggerFactory
			.getLogger(TextHunter.class);

	private int periodId;

	private MessageTextMapper messageTextMapper;

	private static final int DB_FETCH_ROWCOUNT = 30;

	public TextHunter(int thisPeriodId, MessageTextMapper messageTextMapper) {
		super();
		this.periodId = thisPeriodId;
		this.messageTextMapper = messageTextMapper;
	}

	@Override
	public void run() {

		logger.info("回复内容获取定时任务启动，periodId={}", periodId);

		// 数据库加载本次对应的文本数据
		List<String> contentList = getContentList();
		logger.info("共加载回复内容{}条", contentList.size());

		// 覆盖掉上一次的文本内容
		TextContentPool.setCurrentTextList(contentList);

		// 配置好下个时间段即将启动的TaskHunter
		startNextHunter();

	}

	private List<String> getContentList() {
		List<String> allList = new ArrayList<>();

		allList.addAll(getListByPeriodId(0));

		// 为0表示之前并没有定位到有配置的时间
		if (periodId > 0) {
			allList.addAll(getListByPeriodId(periodId));
		}

		return allList;
	}

	private Collection<? extends String> getListByPeriodId(int periodIdArgs) {

		int totalCount = getTotalCount(periodIdArgs);
		int pageCount = CatsAndDogs.pageCount(totalCount, DB_FETCH_ROWCOUNT);

		List<String> thisPeriodList = new ArrayList<>();
		for (int i = 0; i < pageCount; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("periodId", periodIdArgs);
			param.put("startRow", i * DB_FETCH_ROWCOUNT);
			param.put("rowCount", DB_FETCH_ROWCOUNT);
			List<String> thisBatch = messageTextMapper
					.pageContentByParam(param);
			thisPeriodList.addAll(thisBatch);
		}

		return thisPeriodList;
	}

	private int getTotalCount(int periodIdArgs) {
		Map<String, Object> param = new HashMap<>();
		param.put("periodId", periodIdArgs);
		return messageTextMapper.countByParam(param);
	}

	private void startNextHunter() {
		List<TextScdlEntry> workdaySchedule = TextPrepareJob
				.getWorkdaySchedule();

		// 如果为0，说明数据库里对当前时间段并没有做任何配置，所以需要将自己伪装成上一个已有配置的period
		if (periodId == 0) {
			backToPrevPeriodId(workdaySchedule);
		}

		int nextPeriodId = -1;
		TextScdlEntry nextEntry = null;

		// 在经过backToPrevPeriodId之后，还是-1，说明在所有时间区间的最前面，直接指定workdaySchedule中第一个元素就好
		if (periodId == -1) {
			nextPeriodId = workdaySchedule.get(0).periodId;
		} else {
			for (int i = 0; i < workdaySchedule.size(); i++) {

				// 如果找到了自己的位置，说明下一个就是nextPeriodId
				if (workdaySchedule.get(i).periodId == periodId) {
					// 如果超过最大了，也就是说达到顶点了，重新开始
					if (i + 1 >= workdaySchedule.size()) {
						nextPeriodId = 0;
						nextEntry = workdaySchedule.get(0);
					} else {
						nextPeriodId = workdaySchedule.get(i + 1).periodId;
						nextEntry = workdaySchedule.get(i + 1);
					}
					break;
				}
			}
		}

		// 执行一次
		Timer timer = new Timer();
		TextHunter nextHunter = new TextHunter(nextPeriodId, messageTextMapper);
		long delay = (nextEntry.startTime - System.currentTimeMillis());
		
		timer.schedule(nextHunter, delay);

		logger.info("下一个回复内容获取定时任务，periodId={},约{}分钟后执行", nextPeriodId,
				(int) (delay / (60 * 1000)));
		
	}

	/**
	 * |---1---|OOO|OOO|---2---|000|--3---|
	 * 
	 * @param workdaySchedule
	 * @return
	 */
	private void backToPrevPeriodId(List<TextScdlEntry> workdaySchedule) {

		long now = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
				.getTimeInMillis();

		for (int i = 0; i < workdaySchedule.size(); i++) {
			TextScdlEntry thisOne = workdaySchedule.get(i);

			// 落在1
			if (i == 0 && thisOne.startTime > now) {
				periodId = -1;
				break;
			}

			// 落在3
			if (i == (workdaySchedule.size() - 1) && thisOne.endTime < now) {
				periodId = thisOne.periodId;
				break;
			}

			// 落在2
			if ((i + 1) < workdaySchedule.size()) {
				TextScdlEntry nextOne = workdaySchedule.get(i + 1);
				// 落在一个区间里
				if ((thisOne.endTime < now) && (nextOne.startTime > now)) {
					periodId = thisOne.periodId;
					break;
				}
			}

		}

		logger.info("目前时间并没有进行内容配置，定位上一个最近的配置时间区间{}", periodId);
	}

}
