package com.yubo.wechat.content.service.textWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.yubo.wechat.content.dao.MessageTextMapper;
import com.yubo.wechat.content.dao.pojo.MessageText;
import com.yubo.wechat.content.vo.MessageVO;
import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.support.PageUtil;

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
public class TextPoolStartTimer extends TimerTask {

	private TextPool textPool;

	private TextGuide textGuide;

	public TextPoolStartTimer(TextPool textPool,TextGuide textGuide) {
		this.textPool = textPool;
		this.textGuide = textGuide;
	}

	@Override
	public void run() {
		
		//重新加载内容
		List<MessageVO> newContent = reloadContext();
		textPool.setTextContent(newContent);
		
		//让activeTextPool中加入本TextPool
		List<TextPool> activeList = textGuide.getActiveTextPool();
		boolean find =false;
		for (int i = 0; i < activeList.size(); i++) {
			if(activeList.get(i).equals(textPool.getPeriodId())){
				find = true;
				break;
			}
		}
		
		if(!find){
			textGuide.addActivePool(textPool);
		}
	
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
			contentlist.addAll(buildReplyMessageList(textGuide.getMessageTextMapper()
					.pageByParam(contentParam)));
		}

		return contentlist;
	}
	
	private List<MessageVO> buildReplyMessageList(
			List<MessageText> paramList) {
		List<MessageVO> result = new ArrayList<>();
		if(EmptyChecker.isEmpty(paramList)){
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

}
