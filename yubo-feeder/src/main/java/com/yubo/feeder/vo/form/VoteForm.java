package com.yubo.feeder.vo.form;

/**
 * 投票信息编辑form对象
 * 
 * @author yangxy8
 *
 */
public class VoteForm {

	private Integer voteId;

	private String voteTitle;

	private String voteQuestion;

	private String voteDate;

	private String voteAnswerOneDiscription;

	private String voteAnswerOneKey;

	private Integer voteAnswerOneIconId;

	private String voteAnswerTwoDiscription;

	private String voteAnswerTwoKey;

	private Integer voteAnswerTwoIconId;

	public String getVoteQuestion() {
		return voteQuestion;
	}

	public void setVoteQuestion(String voteQuestion) {
		this.voteQuestion = voteQuestion;
	}

	public Integer getVoteId() {
		return voteId;
	}

	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}

	public String getVoteTitle() {
		return voteTitle;
	}

	public void setVoteTitle(String voteTitle) {
		this.voteTitle = voteTitle;
	}

	public String getVoteDate() {
		return voteDate;
	}

	public void setVoteDate(String voteDate) {
		this.voteDate = voteDate;
	}

	public String getVoteAnswerOneDiscription() {
		return voteAnswerOneDiscription;
	}

	public void setVoteAnswerOneDiscription(String voteAnswerOneDiscription) {
		this.voteAnswerOneDiscription = voteAnswerOneDiscription;
	}

	public String getVoteAnswerOneKey() {
		return voteAnswerOneKey;
	}

	public void setVoteAnswerOneKey(String voteAnswerOneKey) {
		this.voteAnswerOneKey = voteAnswerOneKey;
	}

	public String getVoteAnswerTwoDiscription() {
		return voteAnswerTwoDiscription;
	}

	public void setVoteAnswerTwoDiscription(String voteAnswerTwoDiscription) {
		this.voteAnswerTwoDiscription = voteAnswerTwoDiscription;
	}

	public String getVoteAnswerTwoKey() {
		return voteAnswerTwoKey;
	}

	public void setVoteAnswerTwoKey(String voteAnswerTwoKey) {
		this.voteAnswerTwoKey = voteAnswerTwoKey;
	}

	public Integer getVoteAnswerOneIconId() {
		return voteAnswerOneIconId;
	}

	public void setVoteAnswerOneIconId(Integer voteAnswerOneIconId) {
		this.voteAnswerOneIconId = voteAnswerOneIconId;
	}

	public Integer getVoteAnswerTwoIconId() {
		return voteAnswerTwoIconId;
	}

	public void setVoteAnswerTwoIconId(Integer voteAnswerTwoIconId) {
		this.voteAnswerTwoIconId = voteAnswerTwoIconId;
	}

}
