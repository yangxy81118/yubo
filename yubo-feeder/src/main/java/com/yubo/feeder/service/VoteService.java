package com.yubo.feeder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yubo.feeder.dao.VoteBaseMapper;
import com.yubo.feeder.dao.pojo.VoteBase;
import com.yubo.feeder.vo.Query;
import com.yubo.feeder.vo.VoteAnswerViewEntry;
import com.yubo.feeder.vo.VoteVO;

/**
 * SVG CRUD服务
 * 
 * @author yangxy8
 *
 */
@Service
public class VoteService {

	public List<VoteBase> paging(Query<VoteVO> query) {
		query.setPage(query.getPage() == null? 1 : query.getPage());
		query.setRows(query.getRows() == null? 10 : query.getRows());
		query.setSort(query.getSort() == null? "activeDate" : query.getSort());
		query.setOrder(query.getOrder() == null? "desc" : query.getOrder());

		Map<String, Object> param = new HashMap<>();
		param.put("startRow", (query.getPage() - 1) * query.getRows());
		param.put("rowCount", query.getRows());
		param.put("sort", query.getSort());
		param.put("order", query.getOrder());
		List<VoteBase> result = voteBaseMapper.selectByParam(param);
		return result;
	}

	public int getCountForAll() {
		return voteBaseMapper.countByParam(null);
	}

	public void add(VoteVO voteVO) {
		VoteBase voteBase = new VoteBase();
		voteBase.setQuestion(voteVO.getVoteQuestion());
		voteBase.setStartTime(voteVO.getActiveDate());
		voteBase.setTitle(voteVO.getVoteTitle());
		voteBase.setAnswers(buildAnswers(voteVO.getVoteAnswer()));
		voteBase.setLookConfig(buildLookConfig(voteVO));
		voteBaseMapper.insertSelective(voteBase);
	}

	private String buildLookConfig(VoteVO voteVO) {

		JSONObject lookJSON = new JSONObject();
		lookJSON.put("choose-bg-color", "#6b7af7,#353d7d");
		
		List<VoteAnswerViewEntry> answers = voteVO.getVoteAnswer();
		JSONObject answerSvgJSON = new JSONObject();
		for (VoteAnswerViewEntry voteAnswerViewEntry : answers) {
			answerSvgJSON.put(voteAnswerViewEntry.getKey(), voteAnswerViewEntry.getSvgId());
		}
		lookJSON.put("answer-icon", answerSvgJSON);
		return lookJSON.toJSONString();
	}

	private String buildAnswers(List<VoteAnswerViewEntry> voteAnswer) {
		JSONObject answerJSON = new JSONObject();
		for (VoteAnswerViewEntry voteAnswerViewEntry : voteAnswer) {
			answerJSON.put(voteAnswerViewEntry.getDiscription(),
					voteAnswerViewEntry.getKey());
		}
		return answerJSON.toJSONString();
	}

	public void update(VoteVO voteVO) {
		VoteBase voteBase = new VoteBase();
		voteBase.setVoteId((long)voteVO.getVoteId());
		voteBase.setQuestion(voteVO.getVoteQuestion());
		voteBase.setStartTime(voteVO.getActiveDate());
		voteBase.setTitle(voteVO.getVoteTitle());
		voteBase.setAnswers(buildAnswers(voteVO.getVoteAnswer()));
		voteBase.setLookConfig(buildLookConfig(voteVO));
		voteBaseMapper.updateByPrimaryKeySelective(voteBase);
	}

	@Autowired
	VoteBaseMapper voteBaseMapper;

}
