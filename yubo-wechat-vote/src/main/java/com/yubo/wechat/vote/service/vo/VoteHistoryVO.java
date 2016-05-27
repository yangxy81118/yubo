package com.yubo.wechat.vote.service.vo;

public class VoteHistoryVO extends VoteVO {

	private String winnerAnswer;

	private int winnerRate;

	private String userChoice;

	/**
	 * 用来对前端显示的日期进行存储
	 */
	private String startDateForView;

	public String getStartDateForView() {
		return startDateForView;
	}

	public void setStartDateForView(String startDateForView) {
		this.startDateForView = startDateForView;
	}

	public String getUserChoice() {
		return userChoice;
	}

	public void setUserChoice(String userChoice) {
		this.userChoice = userChoice;
	}

	public String getWinnerAnswer() {
		return winnerAnswer;
	}

	public void setWinnerAnswer(String winnerAnswer) {
		this.winnerAnswer = winnerAnswer;
	}

	public int getWinnerRate() {
		return winnerRate;
	}

	public void setWinnerRate(int winnerRate) {
		this.winnerRate = winnerRate;
	}

}
