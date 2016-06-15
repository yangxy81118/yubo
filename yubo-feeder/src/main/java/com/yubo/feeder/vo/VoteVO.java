package com.yubo.feeder.vo;

import java.util.List;

/**
 * 投票
 * 
 * @author yangxy8
 *
 */
public class VoteVO {

	private Long voteId;

	private String voteTitle;

	private String voteQuestion;

	private String activeDate;

	private String colorStyle;
	
	private List<VoteAnswerViewEntry> voteAnswer;

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

	public List<VoteAnswerViewEntry> getVoteAnswer() {
		return voteAnswer;
	}

	public void setVoteAnswer(List<VoteAnswerViewEntry> voteAnswer) {
		this.voteAnswer = voteAnswer;
	}

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public String getColorStyle() {
		return colorStyle;
	}

	public void setColorStyle(String colorStyle) {
		this.colorStyle = colorStyle;
	}

}
