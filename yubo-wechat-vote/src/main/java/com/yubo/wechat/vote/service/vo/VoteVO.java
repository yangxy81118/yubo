package com.yubo.wechat.vote.service.vo;

import java.util.Date;
import java.util.List;

/**
 * 投票数据VO
 * 
 * @author young.jason
 *
 */
public class VoteVO {

	private String voteTitle;

	private String voteQuestion;

	private List<AnswerEntry> voteAnswers;

	private Long voteId;

	private List<AnswerResultEntry> voteResult;

	private Date startTime;

	private Date endTime;

	private String lookConfig;

	public String getLookConfig() {
		return lookConfig;
	}

	public void setLookConfig(String lookConfig) {
		this.lookConfig = lookConfig;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getVoteId() {
		return voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	public String getVoteTitle() {
		return voteTitle;
	}

	public void setVoteTitle(String voteTitle) {
		this.voteTitle = voteTitle;
	}

	public String getVoteQuestion() {
		return voteQuestion;
	}

	public void setVoteQuestion(String voteQuestion) {
		this.voteQuestion = voteQuestion;
	}

	public List<AnswerEntry> getVoteAnswers() {
		return voteAnswers;
	}

	public void setVoteAnswers(List<AnswerEntry> voteAnswers) {
		this.voteAnswers = voteAnswers;
	}

	public List<AnswerResultEntry> getVoteResult() {
		return voteResult;
	}

	public void setVoteResult(List<AnswerResultEntry> voteResult) {
		this.voteResult = voteResult;
	}

}
