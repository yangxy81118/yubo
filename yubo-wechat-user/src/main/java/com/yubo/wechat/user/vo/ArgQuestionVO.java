package com.yubo.wechat.user.vo;

public class ArgQuestionVO {

	private String question;

	private String wrongReply;

	private String answer;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getWrongReply() {
		return wrongReply;
	}

	public void setWrongReply(String wrongReply) {
		this.wrongReply = wrongReply;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "ArgQuestionVO [question=" + question + ", wrongReply="
				+ wrongReply + ", answer=" + answer + "]";
	}

}
