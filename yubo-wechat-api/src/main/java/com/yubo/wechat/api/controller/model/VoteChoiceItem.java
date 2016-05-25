package com.yubo.wechat.api.controller.model;

/**
 * 投票信息展示页关于选项的效果展示
 * 
 * 选项描述，选项关键字，选项投票数，选项百分比，选项背景颜色，选项图标，是否被用户选中
 * 
 * @author young.jason
 *
 */
public class VoteChoiceItem {

	private String choiceDiscription;

	private String choiceAnswer;

	private int voteCount;

	private int rateOfAll;

	private String bgColor;

	private String iconUrl;

	private boolean userAnswer;

	public String getChoiceDiscription() {
		return choiceDiscription;
	}

	public void setChoiceDiscription(String choiceDiscription) {
		this.choiceDiscription = choiceDiscription;
	}

	public String getChoiceAnswer() {
		return choiceAnswer;
	}

	public void setChoiceAnswer(String choiceAnswer) {
		this.choiceAnswer = choiceAnswer;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public int getRateOfAll() {
		return rateOfAll;
	}

	public void setRateOfAll(int rateOfALl) {
		this.rateOfAll = rateOfALl;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public boolean isUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(boolean userAnswer) {
		this.userAnswer = userAnswer;
	}

}
