package com.yubo.wechat.vote.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.vote.dao.UserVoteRecordMapper;
import com.yubo.wechat.vote.dao.VoteBaseMapper;
import com.yubo.wechat.vote.dao.pojo.VoteBase;

/**
 * 投票近实时统计工具 TODO 为了保证数据的一致性，是否考虑本类在启动的时候，对数据库进行一个group by统计
 * 
 * @author young.jason
 *
 */
@Component
public class VoteRealTimeHandler {

	private Map<String, Integer> voteResult = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		
		//首先确保所有的答案关键字都出现在Map中
		Set<String> words = voteAnswerCache.getVoteAnswerWords();
		if(EmptyChecker.isEmpty(words)){
			logger.info("今日无投票，无需加载投票数据");
			return;
		}
		for (String word : words) {
			voteResult.put(word, 0);
		}
		
		// 从VoteAnswerCache中获取今天的投票ID
		Long todayId = voteAnswerCache.getTotayVoteId();
		if (todayId == null) {
			logger.info("今日无投票，无需加载投票数据");
			return;
		}

		// 去数据库中通过groupby拿到最新的统计结果，同步过来
		List<Map<String, Object>> result = userVoteRecordMapper
				.countVoteResult(todayId);
		formatResult(result);
		logger.info("加载今日投票统计完毕");
	}

	private void formatResult(List<Map<String, Object>> result) {
		try {
			for (Map<String, Object> mapEnry : result) {
				voteResult.put(mapEnry.get("answer").toString(),
						Integer.parseInt(mapEnry.get("count").toString()));
			}
		} catch (Exception e) {
			logger.error("加载当日投票统计数据失败," + e.getMessage(), e);
		}
	}

	/**
	 * 实时记录一次投票
	 * 
	 * @param voteWord
	 */
	public void addVote(String voteWord) {
		Integer value = voteResult.get(voteWord);
		value = value == null ? 0 : value;
		voteResult.put(voteWord, value++);
	}

	/**
	 * 修改一次投票
	 * 
	 * @param previousAnswer
	 * @param newAnswer
	 */
	public void updateVote(String previousAnswer, String newAnswer) {
		Integer previousAnswerCount = voteResult.get(previousAnswer);
		voteResult.put(previousAnswer, --previousAnswerCount);
		Integer newAnswerCount = voteResult.get(newAnswer);
		if(newAnswerCount==null){
			newAnswerCount = 0;
		}
		voteResult.put(newAnswer, ++newAnswerCount);
	}

	/**
	 * 获取当前投票的实时结果
	 * 
	 * @return
	 */
	public Map<String, Integer> getVoteResult() {
		return voteResult;
	}

	/**
	 * 清空投票记录并重置最选项记录
	 * 
	 * @param keyword
	 */
	public void clearAndReset(String[] keyword) {
		voteResult.clear();
		for (String k : keyword) {
			voteResult.put(k, 0);
		}
	}

	/**
	 * 定时写入数据库
	 */
	public void scheduledSummary() {

		// 获取todayVoteId
		Long todayVoteId = voteAnswerCache.getTotayVoteId();

		// 如果今日没有投票，直接退出
		if (todayVoteId == null) {
			return;
		}

		// 进行统计，并更新到对应的vote
		// TODO 暂时信赖缓存的结果，根据缓存统计直接写入到数据库
		writeToDB(todayVoteId);

	}

	private void writeToDB(Long todayVoteId) {
		Map<String, Integer> result = getVoteResult();
		Set<Entry<String, Integer>> sets = result.entrySet();

		JSONObject sumJSON = new JSONObject();
		for (Entry<String, Integer> entry : sets) {
			sumJSON.put(entry.getKey(), entry.getValue());
		}

		VoteBase record = new VoteBase();
		record.setVoteId(todayVoteId);
		record.setSummary(sumJSON.toJSONString());
		voteBaseMapper.updateByPrimaryKeySelective(record);
		
		logger.info("投票{}统计写入到数据库",todayVoteId);
	}

	@Autowired
	VoteAnswerCache voteAnswerCache;

	@Autowired
	VoteBaseMapper voteBaseMapper;

	@Autowired
	UserVoteRecordMapper userVoteRecordMapper;

	private static final Logger logger = LoggerFactory
			.getLogger(VoteRealTimeHandler.class);

}
