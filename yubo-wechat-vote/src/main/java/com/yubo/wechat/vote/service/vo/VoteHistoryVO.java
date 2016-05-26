package com.yubo.wechat.vote.service.vo;

public class VoteHistoryVO extends VoteVO {

	private String winnerAnswer;

	private int winnerRate;

	private String userChoice;

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
