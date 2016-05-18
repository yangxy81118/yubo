package com.yubo.wechat.content.service.textspeaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

		// 覆盖掉上一次的文本内容
		TextContentPool.setCurrentTextList(contentList);

		// 配置好下个时间段即将启动的TaskHunter
		startNextHunter();

	}

	private List<String> getContentList() {
		List<String> allList = new ArrayList<>();
		int totalCount = getCount();
		int pageCount = CatsAndDogs.pageCount(totalCount, DB_FETCH_ROWCOUNT);

		for (int i = 0; i < pageCount; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("periodId", 0);
			param.put("startRow", i * DB_FETCH_ROWCOUNT);
			param.put("rowCount", DB_FETCH_ROWCOUNT);
			List<String> thisBatch = messageTextMapper
					.pageContentByParam(param);
			allList.addAll(thisBatch);
		}

		return allList;
	}

	private int getCount() {
		Map<String, Object> param = new HashMap<>();
		param.put("periodId", 0);
		return messageTextMapper.countByParam(param);
	}

	private void startNextHunter() {
		List<TextScdlEntry> workdaySchedule = TextPrepareJob
				.getWorkdaySchedule();

		int nextPeriodId = 0;
		TextScdlEntry nextEntry = null;

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
			}
		}

		logger.info("下一个回复内容获取定时任务，periodId={}", nextPeriodId);

		// 执行一次
		Timer timer = new Timer();
		TextHunter nextHunter = new TextHunter(nextPeriodId, messageTextMapper);
		timer.schedule(nextHunter,
				(nextEntry.startTime - System.currentTimeMillis()));

	}

}
