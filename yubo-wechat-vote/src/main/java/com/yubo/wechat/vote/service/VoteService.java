package com.yubo.wechat.vote.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.yubo.wechat.vote.dao.UserVoteRecordMapper;
import com.yubo.wechat.vote.dao.VoteBaseMapper;
import com.yubo.wechat.vote.dao.pojo.UserVoteRecord;
import com.yubo.wechat.vote.dao.pojo.VoteBase;
import com.yubo.wechat.vote.service.vo.AnswerEntry;
import com.yubo.wechat.vote.service.vo.VoteVO;

/**
 * 投票服务
 * 
 * @author young.jason
 *
 */
@Service
public class VoteService {

	/**
	 * 检查查询文本是否是今日的答案文本<br/>
	 * 
	 * @param word
	 * @return 如果是今日投票答案关键字，则返回今日的投票ID，否则为null
	 */
	public Long getVoteIdByWord(String word) {
		return voteCache.getVoteId(word);
	}
	
	
	public Long getFirstVoteId() {
		return voteCache.getFirstId();
	}
	
	/**
	 * 获取投票信息
	 * 
	 * @param voteId
	 * @return
	 */
	public VoteVO getVoteVOByVoteId(Long voteId){
		VoteBase vote = voteBaseMapper.selectByPrimaryKey(voteId);
		VoteVO vo = new VoteVO();
		vo.setVoteQuestion(vote.getQuestion());
		vo.setVoteAnswers(buildVoteAnswers(vote.getAnswers()));
		return vo;
	}

	private List<AnswerEntry> buildVoteAnswers(String answerTExt) {

		JSONObject json = JSONObject.parseObject(answerTExt);
		Set<Entry<String, Object>> sets = json.entrySet();
		List<AnswerEntry> keys = new ArrayList<>();

		for (Entry<String, Object> entry : sets) {
			AnswerEntry answer = new AnswerEntry();
			answer.setAnswerDiscription(entry.getKey());
			answer.setAnswerKey(entry.getValue().toString());
			keys.add(answer);
		}
		
		return keys;
	}

	/**
	 * 本次投票操作
	 * 
	 * @param answerParam
	 * @return
	 */
	public VoteVO vote(VoteVO answerParam) {

		UserVoteRecord perviousRecord = getPreviousAnswerRecord(
				answerParam.getVoteId(), answerParam.getUserId());

		String previousAnswer = null;
		
		if (perviousRecord == null) {
			addNewAnswer(answerParam.getVoteId(),
					answerParam.getCurrentAnswer(), answerParam.getUserId());
		} else {
			previousAnswer = perviousRecord.getUserChoiceAnswer();
			updateAnswer(perviousRecord, answerParam.getCurrentAnswer());
		}

		String feedBackText = null;
		if (StringUtils.isEmpty(previousAnswer) || previousAnswer.equals(answerParam.getCurrentAnswer())) {
			feedBackText = answerParam.getCurrentAnswer() + "，谢谢你的回答~\n可以点这里看看目前的投票情况哦～";
		} else {
			feedBackText = "你之前的回答是【" + previousAnswer + "】,现在已经改成【"
					+ answerParam.getCurrentAnswer() + "】,谢谢你的回答~\n可以点这里看看目前的投票情况哦～";
		}

		VoteVO voteResult = new VoteVO();
		voteResult.setFeedBackText(feedBackText);
		voteResult.setFeedBackPicUrl(DEFAULT_FEEDBACK_PIC_URL);
		return voteResult;
	}

	private void updateAnswer(UserVoteRecord perviousRecord,
			String currentAnswer) {
		perviousRecord.setUserChoiceAnswer(currentAnswer);
		userVoteRecordMapper.updateByPrimaryKeySelective(perviousRecord);
	}

	private void addNewAnswer(Long voteId, String currentAnswer, int userId) {

		UserVoteRecord record = new UserVoteRecord();
		record.setUserId(userId);
		record.setVoteId(voteId);
		record.setUserChoiceAnswer(currentAnswer);

		userVoteRecordMapper.insertSelective(record);

	}

	private UserVoteRecord getPreviousAnswerRecord(Long voteId, int userId) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("voteId", voteId);
		param.put("userId", userId);
		param.put("startRow", 0);
		param.put("rowCount", 1);
		
		List<UserVoteRecord> result = userVoteRecordMapper.selectByParam(param);
		if (result == null || result.size() <= 0) {
			return null;
		}
		UserVoteRecord resultVO = result.get(0);
		return resultVO;
	}

	@Autowired
	VoteCache voteCache;

	@Autowired
	UserVoteRecordMapper userVoteRecordMapper;
	
	@Autowired
	VoteBaseMapper voteBaseMapper;

	private static final String DEFAULT_FEEDBACK_PIC_URL = "http://img.taopic.com/uploads/allimg/130611/235071-130611193G551.jpg";

}
