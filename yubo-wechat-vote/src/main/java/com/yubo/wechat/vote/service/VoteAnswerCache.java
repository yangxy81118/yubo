package com.yubo.wechat.vote.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yubo.wechat.support.PageUtil;
import com.yubo.wechat.vote.dao.VoteBaseMapper;
import com.yubo.wechat.vote.dao.pojo.VoteBase;

/**
 * 投票内容的映射<br/>
 * 每天6点准时启动，读取今天<br/>
 * TODO 或许这里还需要考虑如果当天做修改
 * 
 * @author young.jason
 *
 */
@Component
public class VoteAnswerCache {

	private Map<String, Long> answer4VoteMapping = new HashMap<>();

	private Long totayVoteId = null;

	@PostConstruct
	public void loadMapping() {

		answer4VoteMapping.clear();

		logger.info("开始加载今日的投票ID与答案关键字的映射");
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
				}
			}
		}

		totayVoteId = getFirstId();

		logger.info("今日的投票ID与答案关键字的映射加载完毕，一共{}条数据", answer4VoteMapping.size());

	}

	public Set<String> getVoteAnswerWords(){
		if(answer4VoteMapping!=null){
			return answer4VoteMapping.keySet();
		}
		return null;
	}
	
	public Long getTotayVoteId() {
		return totayVoteId;
	}

	private Date getVoteTime(int offsetHours) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
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

	@Autowired
	VoteBaseMapper voteBaseMapper;

	private static final Logger logger = LoggerFactory
			.getLogger(VoteAnswerCache.class);

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

}
