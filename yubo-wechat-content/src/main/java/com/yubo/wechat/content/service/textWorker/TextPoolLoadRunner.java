package com.yubo.wechat.content.service.textWorker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubo.wechat.content.dao.MessageTextMapper;
import com.yubo.wechat.content.dao.pojo.MessageText;
import com.yubo.wechat.content.vo.MessageVO;
import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.support.MathUtil;
import com.yubo.wechat.support.PageUtil;
import com.yubo.wechat.support.TimeUtil;

/**
 * 加载内容池的内容 同时进行定时开关的任务
 * 
 * @author yangxy8
 *
 */
public class TextPoolLoadRunner implements Callable<Integer> {

	private TextPool textPool;
	private TextGuide textGuide;
	private MessageTextMapper messageTextMapper;

	public TextPoolLoadRunner(TextPool textPool, TextGuide textGuide,
			MessageTextMapper messageTextMapper) {
		this.textPool = textPool;
		this.textGuide = textGuide;
		this.messageTextMapper = messageTextMapper;
	}

	@Override
	public Integer call() throws Exception {

		// 加载数据
		Map<String, Object> param = new HashMap<>();
		param.put("periodId", textPool.getPeriodId());
		int totalRows = messageTextMapper.countByParam(param);

		if (totalRows == 0) {
			return totalRows;
		}

		int pageCount = PageUtil.pageCount(totalRows, PAGE_ROWS);
		List<MessageVO> contentlist = new ArrayList<>();

		for (int i = 0; i < pageCount; i++) {
			Map<String, Object> contentParam = new HashMap<>();
			contentParam.put("periodId", textPool.getPeriodId());
			contentParam.put("startRow", i * PAGE_ROWS);
			contentParam.put("rowCount", PAGE_ROWS);
			contentlist.addAll(buildReplyMessageList(messageTextMapper
					.pageByParam(contentParam)));
		}

		textPool.setTextContent(contentlist);

		// 加载定时任务
		startTimer();

		return totalRows;
	}

	private List<MessageVO> buildReplyMessageList(List<MessageText> paramList) {
		List<MessageVO> result = new ArrayList<>();
		if (EmptyChecker.isEmpty(paramList)) {
			return result;
		}

		for (MessageText message : paramList) {
			MessageVO vo = new MessageVO();
			vo.setContent(message.getContent());
			vo.setFunctionCode(message.getFuncCode());
			result.add(vo);
		}

		return result;
	}

	/**
	 * 启动定时任务<br/>
	 * 这里会有三种情况:<br/>
	 * ----|-----A-----|----B--(NOW)---|---C----|---
	 * A:结束时间都早于当前系统时间，则表示定时任务肯定要从明天才开始
	 * B:结束时间晚于系统时间，但开始时间早于系统时间，则说明当前系统应该生效的，所以结束任务今天启动，开始任务明天启动
	 * C:开始时间晚于系统时间，则开始与结束任务均为当天执行
	 */
	private void startTimer() {

		Calendar cal = TimeUtil.getChinaCalendar();
		int nowHour = cal.get(Calendar.HOUR_OF_DAY);

		if (textPool.getEndHour() <= nowHour) {
			prepareTimerForA(cal);
		} else if (textPool.getEndHour() > nowHour
				&& textPool.getStartHour() <= nowHour) {
			prepareTimerForB(cal);
		} else {
			prepareTimerForC(cal);
		}
	}

	private void prepareTimerForC(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, textPool.getStartHour());
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		Timer startTimer = new Timer();
		Date startDate = cal.getTime();
		startTimer.schedule(new TextPoolStartTimer(textPool, textGuide),
				startDate);
		logger.info("TextPool[{}]在{}执行下一次Start任务", textPool.getPeriodId(),
				TimeUtil.formatTime(startDate));

		cal.set(Calendar.HOUR_OF_DAY, textPool.getEndHour());
		Timer endTimer = new Timer();
		Date endDate = cal.getTime();
		endTimer.schedule(new TextPoolEndTimer(textPool, textGuide), endDate);
		logger.info("TextPool[{}]在{}执行下一次End任务", textPool.getPeriodId(),
				TimeUtil.formatTime(endDate));
	}

	private void prepareTimerForB(Calendar cal) {

		cal.set(Calendar.HOUR_OF_DAY, textPool.getEndHour());
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Timer endTimer = new Timer();
		Date endDate = cal.getTime();
		endTimer.schedule(new TextPoolEndTimer(textPool, textGuide), endDate);
		logger.info("TextPool[{}]在{}执行下一次End任务", textPool.getPeriodId(),
				TimeUtil.formatTime(endDate));

		// 如果是24，会导致Date数据往后延一天
		if (textPool.getEndHour().equals(24)) {
			cal.add(Calendar.DATE, -1);
		}

		// 开始时间推迟一天
		cal.set(Calendar.HOUR_OF_DAY, textPool.getStartHour());
		cal.add(Calendar.DATE, 1);
		Timer startTimer = new Timer();
		Date startDate = cal.getTime();
		startTimer.schedule(new TextPoolStartTimer(textPool, textGuide),
				startDate);
		logger.info("TextPool[{}]在{}执行下一次Start任务", textPool.getPeriodId(),
				TimeUtil.formatTime(startDate));

		textGuide.addActivePool(textPool);
	}

	private void prepareTimerForA(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, textPool.getStartHour());
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DATE, 1);

		Timer startTimer = new Timer();
		Date startDate = cal.getTime();
		startTimer.schedule(new TextPoolStartTimer(textPool, textGuide),
				startDate);
		logger.info("TextPool[{}]在{}执行下一次Start任务", textPool.getPeriodId(),
				TimeUtil.formatTime(startDate));

		cal.set(Calendar.HOUR_OF_DAY, textPool.getEndHour());
		Timer endTimer = new Timer();
		Date endDate = cal.getTime();
		endTimer.schedule(new TextPoolEndTimer(textPool, textGuide), endDate);
		logger.info("TextPool[{}]在{}执行下一次End任务", textPool.getPeriodId(),
				TimeUtil.formatTime(endDate));

	}

	private static final int PAGE_ROWS = 30;

	private static final Logger logger = LoggerFactory
			.getLogger(TextPoolLoadRunner.class);

}
