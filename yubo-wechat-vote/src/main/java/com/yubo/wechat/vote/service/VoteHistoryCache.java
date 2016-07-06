package com.yubo.wechat.vote.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.yubo.wechat.vote.dao.VoteBaseMapper;
import com.yubo.wechat.vote.dao.pojo.VoteBase;
import com.yubo.wechat.vote.service.vo.AnswerResultEntry;
import com.yubo.wechat.vote.service.vo.VoteHistoryVO;
import com.yubo.wechat.vote.util.VoteUtil;

/**
 * 用来缓存最近N次投票记录
 * 
 * @author young.jason
 *
 */
@Component
public class VoteHistoryCache {

	@Value("${vote.history.recent.rows:20}")
	private int recentRows;

	private List<VoteHistoryVO> historyList = new ArrayList<>();

	/**
	 * 定时加载投票历史记录<br/>
	 * 由于目前投票都是次日6点结束，所以会在每天6点5分执行统计
	 * 
	 */
	@PostConstruct
	public synchronized void loadHistory() {

		List<VoteHistoryVO> tmpList = new ArrayList<>();
		
		logger.info("开始加载投票历史结果数据");
		Map<String, Object> param = new HashMap<>();
		param.put("startRow", 0);
		param.put("rowCount", recentRows);
		param.put("timeEnd", getTimeEnd());
		param.put("orderField", "vote_id");
		param.put("orderType", "DESC");
		List<VoteBase> vbList = voteBaseMapper.selectByParam(param);

		for (VoteBase voteBase : vbList) {
			VoteHistoryVO vo = new VoteHistoryVO();
			vo.setVoteQuestion(voteBase.getQuestion());
			vo.setVoteId(voteBase.getVoteId());
			buildWinner(vo, voteBase.getSummary());
			vo.setStartTime(voteBase.getStartTime());
			tmpList.add(vo);
		}
		
		historyList = null;
		historyList = tmpList;
		
		logger.info("加载投票历史结果数据完毕，共加载{}条", historyList.size());
	}

	private Date getTimeEnd() {
		
		//如果当下没有正在进行的投票，则表示所有的投票都是历史，无需此参数
		if(voteRealTimeHandler.getActiveVoteId()==null){
			return null;
		}
		else{
			//如果当下有正在进行的投票，则要获取当下投票的开始日期，倒退一天，并在此日期基础加30小时
			Calendar cal = VoteUtil.getVoteEndTime(voteRealTimeHandler.getActiveVoteDate());
			cal.add(Calendar.DATE, -1);
			return cal.getTime();
		}
	}

	/**
	 * 获取历史投票记录
	 * 
	 * @return
	 */
	public synchronized List<VoteHistoryVO> getHistoryList() {
		return historyList;
	}

	/**
	 * 构建胜利方数据
	 * 
	 * @param historyVO
	 * @param summary
	 */
	private void buildWinner(VoteHistoryVO historyVO, String summary) {
		if (StringUtils.isEmpty(summary)) {
			return;
		}
		AnswerResultEntry entry = VoteUtil.getWinner(summary);
		historyVO.setWinnerAnswer(entry.getKey());
		historyVO.setWinnerRate(entry.getRate());
	}

	@Autowired
	VoteBaseMapper voteBaseMapper;
	
	@Autowired
	VoteRealTimeHandler voteRealTimeHandler;

	private static final Logger logger = LoggerFactory
			.getLogger(VoteHistoryCache.class);
}
