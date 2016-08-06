package com.yubo.wechat.user.vo;

/**
 * 
 * @author yangxy8
 *
 */
public class ArgPlayVO {

	private Integer userProgressId;
	
	private Integer userId;

	private Integer gameTopicId;

	/**
	 * 用户输入的内容
	 */
	private String userMessage;

	private Integer currentStep;

	private ArgQuestionVO argQuestionVO;

	private Integer authIdentificationId;

	public Integer getUserProgressId() {
		return userProgressId;
	}

	public void setUserProgressId(Integer userProgressId) {
		this.userProgressId = userProgressId;
	}

	public Integer getAuthIdentificationId() {
		return authIdentificationId;
	}

	public void setAuthIdentificationId(Integer authIdentificationId) {
		this.authIdentificationId = authIdentificationId;
	}

	public Integer getGameTopicId() {
		return gameTopicId;
	}

	public void setGameTopicId(Integer gameTopicId) {
		this.gameTopicId = gameTopicId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public Integer getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}

	public ArgQuestionVO getArgQuestionVO() {
		return argQuestionVO;
	}

	public void setArgQuestionVO(ArgQuestionVO argQuestionVO) {
		this.argQuestionVO = argQuestionVO;
	}

}
