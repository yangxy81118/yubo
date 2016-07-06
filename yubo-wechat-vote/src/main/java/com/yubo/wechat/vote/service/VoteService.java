package com.yubo.wechat.vote.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.vote.dao.UserVoteRecordMapper;
import com.yubo.wechat.vote.dao.VoteBaseMapper;
import com.yubo.wechat.vote.dao.pojo.UserVoteRecord;
import com.yubo.wechat.vote.dao.pojo.VoteBase;
import com.yubo.wechat.vote.service.vo.AnswerEntry;
import com.yubo.wechat.vote.service.vo.AnswerResultEntry;
import com.yubo.wechat.vote.service.vo.UserVoteVO;
import com.yubo.wechat.vote.service.vo.VoteHistoryVO;
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
		return voteRealTimeHandler.getVoteId(word);
	}

	public Long getFirstVoteId() {
		return voteRealTimeHandler.getActiveVoteId();
	}

	/**
	 * 根据用户ID与参与的投票ID获取用户当次投票信息
	 * 
	 * @param userId
	 * @param voteId
	 * @return
	 */
	public UserVoteVO getVoteAnswerForUser(Integer userId, Long voteId) {

		UserVoteVO userVoteVO = new UserVoteVO();

		// 首先应该还是先通过voteId获取投票信息
		VoteBase voteBase = voteBaseMapper.selectByPrimaryKey(voteId);
		VoteVO voteVO = new VoteVO();
		voteVO.setVoteId(voteId);
		voteVO.setVoteQuestion(voteBase.getQuestion());
		voteVO.setVoteTitle(voteBase.getTitle());
		voteVO.setVoteAnswers(buildAnswer(voteBase));
		voteVO.setEndTime(voteBase.getEndTime());
		voteVO.setStartTime(voteBase.getStartTime());
		voteVO.setLookConfig(voteBase.getLookConfig());

		// 如果是当日进行中的投票，则直接从缓存中拿取
		if (voteId.equals(voteRealTimeHandler.getActiveVoteId())) {
			voteVO.setVoteResult(buildAnswerResult(voteRealTimeHandler
					.getVoteResult()));
		} else {
			voteVO.setVoteResult(buildAnswerResult(voteBase.getSummary()));
		}

		userVoteVO.setVoteVO(voteVO);

		if (userId == null) {
			return userVoteVO;
		}

		// 然后再根据UserId进行查询
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("voteId", voteId);
		param.put("startRow", 0);
		param.put("rowCount", 1);

		List<UserVoteRecord> result = userVoteRecordMapper.selectByParam(param);
		if (EmptyChecker.isEmpty(result)) {
			return userVoteVO;
		}

		// 用户投票内容
		UserVoteRecord record = result.get(0);
		userVoteVO.setCurrentAnswer(record.getUserChoiceAnswer());
		userVoteVO.setUserId(userId);

		return userVoteVO;
	}

	/**
	 * 返回最近N次用户的投票记录
	 * 
	 * @param recentRows
	 * @return Key-投票ID，Value-用户投票记录
	 */
	public Map<Long, UserVoteVO> getUserVoteList(int recentRows, int userId) {

		List<UserVoteRecord> list = getPreviousAnswerRecord(null, userId, 0,
				recentRows);
		Map<Long, UserVoteVO> map = new HashMap<>();
		if(list==null){
			return map;
		}
		
		for (UserVoteRecord userVoteRecord : list) {
			UserVoteVO vote = new UserVoteVO();
			vote.setCurrentAnswer(userVoteRecord.getUserChoiceAnswer());
			map.put(userVoteRecord.getVoteId(), vote);
		}

		return map;
	}

	private List<AnswerResultEntry> buildAnswerResult(
			Map<String, Integer> voteResult) {
		List<AnswerResultEntry> list = new ArrayList<>();
		Set<Entry<String, Integer>> answers = voteResult.entrySet();
		for (Entry<String, Integer> entry : answers) {
			AnswerResultEntry a = new AnswerResultEntry();
			a.setCount(Integer.parseInt(entry.getValue().toString()));
			a.setKey(entry.getKey());
			list.add(a);
		}
		return list;
	}

	private List<AnswerResultEntry> buildAnswerResult(String summary) {

		List<AnswerResultEntry> list = new ArrayList<>();
		JSONObject json = JSONObject.parseObject(summary);
		Set<Entry<String, Object>> answers = json.entrySet();
		for (Entry<String, Object> entry : answers) {
			AnswerResultEntry a = new AnswerResultEntry();
			a.setCount(Integer.parseInt(entry.getValue().toString()));
			a.setKey(entry.getKey());
			list.add(a);
		}
		return list;

	}

	private List<AnswerEntry> buildAnswer(VoteBase voteBase) {

		List<AnswerEntry> list = new ArrayList<>();
		JSONObject json = JSONObject.parseObject(voteBase.getAnswers());
		Set<Entry<String, Object>> answers = json.entrySet();
		for (Entry<String, Object> entry : answers) {
			AnswerEntry a = new AnswerEntry();
			a.setAnswerDiscription(entry.getKey());
			a.setAnswerKey(entry.getValue().toString());
			list.add(a);
		}
		return list;
	}

	/**
	 * 获取投票信息
	 * 
	 * @param voteId
	 * @return
	 */
	public VoteVO getVoteInfoByVoteId(Long voteId) {
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
	 * 本次投票操作 TODO 这里也是一大堆的事务
	 * 
	 * @param answerParam
	 * @return
	 */
	public UserVoteVO vote(UserVoteVO answerParam) {

		// 之前是否已经投票过?
		
		
		
		List<UserVoteRecord> voteHistory = getPreviousAnswerRecord(answerParam
				.getVoteVO().getVoteId(), answerParam.getUserId(), 0, 1);
		UserVoteRecord perviousRecord = null;
		if (voteHistory != null && voteHistory.size() > 0) {
			perviousRecord = voteHistory.get(0);
		}

		String previousAnswer = null;
		String newAnswer = answerParam.getCurrentAnswer();

		// 查询新投票记录或者更新投票选项
		if (perviousRecord == null) {
			addNewAnswer(answerParam.getVoteVO().getVoteId(), newAnswer,
					answerParam.getUserId());
			// 增加到缓存
			voteRealTimeHandler.addVote(newAnswer);

		} else {
			previousAnswer = perviousRecord.getUserChoiceAnswer();
			if (!previousAnswer.equals(newAnswer)) {
				updateAnswer(perviousRecord, newAnswer);
				// 修改缓存
				voteRealTimeHandler.updateVote(previousAnswer, newAnswer);
			}
		}

		// 构建返回信息
		StringBuffer feedBackText = new StringBuffer();
		if (StringUtils.isEmpty(previousAnswer)
				|| previousAnswer.equals(newAnswer)) {
			feedBackText.append("你选择【").append(newAnswer)
					.append("】，谢谢~\n可以点这里看看目前的投票情况哦～");
		} else {
			feedBackText.append("你之前的回答是【").append(previousAnswer);
			feedBackText.append("】\n现在已经改成【").append(newAnswer)
					.append("】,谢谢~\n可以点这里看看目前的投票情况哦~");
		}

		UserVoteVO voteResult = new UserVoteVO();
		voteResult.setFeedBackText(feedBackText.toString());
		voteResult.setFeedBackPicUrl(DEFAULT_FEEDBACK_PIC_URL);
		voteResult.setUserId(answerParam.getUserId());
		VoteVO voteVO = new VoteVO();
		voteVO.setVoteId(answerParam.getVoteVO().getVoteId());
		voteResult.setVoteVO(voteVO);

		return voteResult;
	}

	/**
	 * 最近投票列表
	 * 
	 * @param rowCount
	 * @return
	 */
	public List<VoteHistoryVO> recentList(int rowCount) {

		// 首先从缓存里获取历史数据
		List<VoteHistoryVO> recentList = new LinkedList<>();
		recentList.addAll(voteHistoryCache.getHistoryList());
		
		// 再从当前的投票记录中获取
		VoteHistoryVO activeVoteVO = new VoteHistoryVO();
		if (voteRealTimeHandler.getActiveVoteId() != null) {
			activeVoteVO.setStartTime(voteRealTimeHandler.getActiveVoteDate());
			activeVoteVO.setVoteQuestion("想知道今天的问题吗~?快去大屏幕看看YUBO吧~");
		}else{
			activeVoteVO.setStartTime(new Date());
			activeVoteVO.setVoteQuestion("今天YUBO很懒，没有什么问题~zZZ...");
		}
		recentList.add(0, activeVoteVO);

		return recentList;
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

	private List<UserVoteRecord> getPreviousAnswerRecord(Long voteId,
			int userId, int startRow, int rowCount) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("voteId", voteId);
		param.put("userId", userId);
		param.put("startRow", startRow);
		param.put("rowCount", rowCount);
		param.put("sort", "voteId");
		param.put("order", "desc");

		List<UserVoteRecord> result = userVoteRecordMapper.selectByParam(param);
		if (result == null || result.size() <= 0) {
			return null;
		}
		return result;
	}

	@Autowired
	UserVoteRecordMapper userVoteRecordMapper;

	@Autowired
	VoteBaseMapper voteBaseMapper;

	@Autowired
	VoteRealTimeHandler voteRealTimeHandler;

	@Autowired
	VoteHistoryCache voteHistoryCache;

	// TODO 需要配置
	private static final String DEFAULT_FEEDBACK_PIC_URL = "http://img.taopic.com/uploads/allimg/130611/235071-130611193G551.jpg";

}
