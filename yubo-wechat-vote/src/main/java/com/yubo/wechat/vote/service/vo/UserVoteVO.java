package com.yubo.wechat.vote.service.vo;

import java.util.List;

/**
 * 投票数据VO
 * 
 * @author young.jason
 *
 */
public class UserVoteVO {

	private VoteVO voteVO;

	private Integer userId;

	private String currentAnswer;

	private String previousAnswer;

	private String openAnswer;

	/**
	 * 反馈谢谢的文字
	 */
	private String feedBackText;

	/**
	 * 反馈谢谢的图片url
	 */
	private String feedBackPicUrl;

	/**
	 * 投票反馈结果H5页面地址
	 */
	private String feedBackViewUrl;

	public String getFeedBackViewUrl() {
		return feedBackViewUrl;
	}

	public void setFeedBackViewUrl(String feedBackViewUrl) {
		this.feedBackViewUrl = feedBackViewUrl;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCurrentAnswer() {
		return currentAnswer;
	}

	public void setCurrentAnswer(String currentAnswer) {
		this.currentAnswer = currentAnswer;
	}

	public String getPreviousAnswer() {
		return previousAnswer;
	}

	public void setPreviousAnswer(String previousAnswer) {
		this.previousAnswer = previousAnswer;
	}

	public String getOpenAnswer() {
		return openAnswer;
	}

	public void setOpenAnswer(String openAnswer) {
		this.openAnswer = openAnswer;
	}

	public String getFeedBackText() {
		return feedBackText;
	}

	public void setFeedBackText(String feedBackText) {
		this.feedBackText = feedBackText;
	}

	public String getFeedBackPicUrl() {
		return feedBackPicUrl;
	}

	public void setFeedBackPicUrl(String feedBackPicUrl) {
		this.feedBackPicUrl = feedBackPicUrl;
	}

	public VoteVO getVoteVO() {
		return voteVO;
	}

	public void setVoteVO(VoteVO voteVO) {
		this.voteVO = voteVO;
	}

}
