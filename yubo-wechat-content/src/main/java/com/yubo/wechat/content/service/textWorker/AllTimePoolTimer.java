package com.yubo.wechat.content.service.textWorker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yubo.wechat.content.dao.pojo.MessageText;
import com.yubo.wechat.content.vo.MessageVO;
import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.support.PageUtil;
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
public class AllTimePoolTimer extends TimerTask {

	private TextPool textPool;

	private TextGuide textGuide;

	public AllTimePoolTimer(TextPool textPool, TextGuide textGuide) {
		this.textPool = textPool;
		this.textGuide = textGuide;
	}

	@Override
	public void run() {

		// 重新加载内容
		List<MessageVO> newContent = reloadContext();
		textPool.setTextContent(newContent);
		// 每天0点生效
		Calendar cal = TimeUtil.getChinaCalendar(1, 0, 0, 0);
		Timer timer = new Timer();
		timer.schedule(new AllTimePoolTimer(textPool, textGuide), cal.getTime());
		logger.info("TextPool[0]在明天0点执行更新任务");
	}

	private List<MessageVO> reloadContext() {
		List<MessageVO> contentlist = new ArrayList<>();

		Map<String, Object> param = new HashMap<>();
		param.put("periodId", textPool.getPeriodId());
		int totalRows = textGuide.getMessageTextMapper().countByParam(param);

		if (totalRows == 0) {
			return contentlist;
		}

		int pageCount = PageUtil.pageCount(totalRows, PAGE_ROWS);

		for (int i = 0; i < pageCount; i++) {
			Map<String, Object> contentParam = new HashMap<>();
			contentParam.put("periodId", textPool.getPeriodId());
			contentParam.put("startRow", i * PAGE_ROWS);
			contentParam.put("rowCount", PAGE_ROWS);
			contentlist.addAll(buildReplyMessageList(textGuide
					.getMessageTextMapper().pageByParam(contentParam)));
		}

		return contentlist;
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

	private static final int PAGE_ROWS = 30;
	
	private static final Logger logger = LoggerFactory
			.getLogger(AllTimePoolTimer.class);

}
