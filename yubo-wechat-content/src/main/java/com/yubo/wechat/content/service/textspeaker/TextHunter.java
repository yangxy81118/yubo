package com.yubo.wechat.content.service.textspeaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 去Database加载自身对应的的文本内容<br/>
 * 本结构特点在于链式处理，每个时间段任务执行完后，主动配置下一个时间段任务
 * 
 * @author young.jason
 *
 */
public class TextHunter implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(TextHunter.class);

	private int thisPeriodId;

	private TextHunter nextHunter;

	private TextScdlEntry timeEntry;

	public TextHunter(int thisPeriodId, TextHunter nextHunter,
			TextScdlEntry timeEntry) {
		super();
		this.thisPeriodId = thisPeriodId;
		this.nextHunter = nextHunter;
		this.timeEntry = timeEntry;
	}

	@Override
	public void run() {
		
		//先去数据库加载本次对应的文本数据
		
		//然后去掉上个时间段的文本内容
		
		//配置好下个时间段即将启动的TaskHunter
		
		
	}

}
