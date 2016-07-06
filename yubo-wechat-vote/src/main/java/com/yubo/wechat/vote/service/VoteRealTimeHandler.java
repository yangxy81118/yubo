package com.yubo.wechat.vote.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.support.PageUtil;
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
	
	private Map<String, Long> answer4VoteMapping = new HashMap<>();

	private Long activeVoteId = null;
	
	private Date activeVoteDate = null;

	@PostConstruct
	public void init() {
		
		loadMapping();
		
		//---------------TODO 需要对这段代码进行结构优化-----------------------------------------------------------
		
		//首先确保所有的答案关键字都出现在Map中
		Set<String> words = getVoteAnswerWords();
		if(EmptyChecker.isEmpty(words)){
			logger.info("今日无投票，无需加载投票数据");
			return;
		}
		for (String word : words) {
			voteResult.put(word, 0);
		}
		
		// 从VoteAnswerCache中获取今天的投票ID
		Long todayId = getActiveVoteId();
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
	
	public void loadMapping(){
		
		logger.info("开始加载今日的投票ID与答案关键字的映射");
		answer4VoteMapping.clear();

		int totalCount = getCount();
		int pageCount = PageUtil.pageCount(totalCount, PAGE_SIZE);
		for (int i = 0; i < pageCount; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("startRow", i * PAGE_SIZE);
			param.put("rowCount", PAGE_SIZE);
			param.put("timeFrom", getVoteTime(6));
			param.put("timeEnd", getVoteTime(30));
			List<VoteBase> list = voteBaseMapper.selectByParam(param);
			for (VoteBase voteBase : list) {
				String[] keys = parseKeys(voteBase.getAnswers());
				for (int j = 0; j < keys.length; j++) {
					answer4VoteMapping.put(keys[j], voteBase.getVoteId());
					voteResult.put(keys[j], 0);
				}
				break;
			}
		}
		
		activeVoteId = getFirstId();
		activeVoteDate = getVoteDate();
		logger.info("今日的投票ID与答案关键字的映射加载完毕，投票答案为{}", answer4VoteMapping);
	}

	/**
	 * 获取当前正在进行中的投票的日期<br/>
	 * 注意，请只使用年月日，十分秒不准确
	 * @return
	 */
	public Date getActiveVoteDate() {
		return activeVoteDate;
	}

	private Date getVoteDate() {
		if(activeVoteId==null){
			return null;
		}
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		//如果是凌晨0-6点之间，则需要日期往回走一天
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if(hour < 6 && hour >= 0){
			cal.add(Calendar.DATE, -1);
		}
		
		return cal.getTime();
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
		logger.info("之前{}的票数{}",voteWord,value);
		value++;
		voteResult.put(voteWord, value);
		logger.info("投票后的情况{}",voteResult);
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
		logger.info("投票后的情况{}",voteResult);
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

		// 获取正在进行中的投票ID
		Long todayVoteId = getActiveVoteId();

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

	public Set<String> getVoteAnswerWords(){
		if(answer4VoteMapping!=null){
			return answer4VoteMapping.keySet();
		}
		return null;
	}
	
	public Long getActiveVoteId() {
		return activeVoteId;
	}

	private Date getVoteTime(int offsetHours) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		//如果是凌晨0-6点之间，则需要日期往回走一天
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if(hour < 6 && hour >= 0){
			cal.add(Calendar.DATE, -1);
		}
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.HOUR, offsetHours);
		return cal.getTime();
	}

	private String[] parseKeys(String answers) {

		JSONObject json = JSONObject.parseObject(answers);
		Set<Entry<String, Object>> sets = json.entrySet();
		String[] keys = new String[sets.size()];

		int idx = 0;
		for (Entry<String, Object> entry : sets) {
			keys[idx++] = entry.getValue().toString();
		}

		return keys;
	}

	private int getCount() {
		return voteBaseMapper.countByParam(new VoteBase());
	}

	/**
	 * 根据投票关键字获取对应的投票ID
	 * 
	 * @param word
	 * @return
	 */
	public synchronized Long getVoteId(String word) {
		return answer4VoteMapping.get(word);
	}

	private static final int PAGE_SIZE = 30;

	private Long getFirstId() {

		Set<String> sets = answer4VoteMapping.keySet();
		Long id = null;
		for (String k : sets) {
			id = answer4VoteMapping.get(k);
			if (id != null) {
				break;
			}
		}
		return id;
	}

	@Autowired
	VoteBaseMapper voteBaseMapper;

	@Autowired
	UserVoteRecordMapper userVoteRecordMapper;

	private static final Logger logger = LoggerFactory
			.getLogger(VoteRealTimeHandler.class);

}
