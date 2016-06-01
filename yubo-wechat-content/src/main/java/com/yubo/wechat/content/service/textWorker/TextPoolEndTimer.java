package com.yubo.wechat.content.service.textWorker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubo.wechat.content.dao.MessageTextMapper;
import com.yubo.wechat.support.MathUtil;
import com.yubo.wechat.support.TimeUtil;

/**
 * 内容池的重加载任务处理<br/>
 * 任务分成两种: 启动：<br/>
 * 启动是指从数据库中加载最新的内容到TextPool中,并告知TextGuide自己生效了
 * 
 * 停止：<br/>
 * 停止是指告知TextGuide停止对当前TextPool的引用
 * 
 * @author yangxy8
 *
 */
public class TextPoolEndTimer extends TimerTask {

	private TextPool textPool;

	private TextGuide textGuide;

	public TextPoolEndTimer(TextPool textPool, TextGuide textGuide) {
		this.textPool = textPool;
		this.textGuide = textGuide;
	}

	@Override
	public void run() {

		// 删除activePool中的Pool
		textGuide.removeActivePool(textPool);

		// 同时开启明天的定时任务
		Calendar cal = TimeUtil.getChinaCalendar();
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

	private static final Logger logger = LoggerFactory
			.getLogger(TextPoolEndTimer.class);

}
