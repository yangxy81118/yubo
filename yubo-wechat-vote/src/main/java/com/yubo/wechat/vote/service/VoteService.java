package com.yubo.wechat.vote.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yubo.wechat.vote.dao.UserVoteRecordMapper;
import com.yubo.wechat.vote.dao.pojo.UserVoteRecord;
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

	/**
	 * 本次投票操作
	 * 
	 * @param answerParam
	 * @return
	 */
	public VoteVO vote(VoteVO answerParam) {

		UserVoteRecord perviousRecord = getPreviousAnswerRecord(
				answerParam.getVoteId(), answerParam.getUserId());

		if (perviousRecord == null) {
			addNewAnswer(answerParam.getVoteId(),
					answerParam.getCurrentAnswer(), answerParam.getUserId());
		} else {
			updateAnswer(perviousRecord, answerParam.getCurrentAnswer());
		}

		String feedBackText = null;
		if (StringUtils.isEmpty(perviousRecord.getUserChoiceAnswer()) || perviousRecord.getUserChoiceAnswer().equals(answerParam.getCurrentAnswer())) {
			feedBackText = answerParam.getCurrentAnswer() + "，嗯嗯，谢谢你的回答~";
		} else {
			feedBackText = "你之前的回答是[" + perviousRecord + "],现在已经改成["
					+ answerParam.getCurrentAnswer() + "],谢谢你的回答~";
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

	private static final String DEFAULT_FEEDBACK_PIC_URL = "http://img.taopic.com/uploads/allimg/130611/235071-130611193G551.jpg";

}
