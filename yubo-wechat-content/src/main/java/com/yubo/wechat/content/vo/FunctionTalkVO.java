package com.yubo.wechat.content.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能性交流VO
 * 
 * @author yangxy8
 *
 */
public class FunctionTalkVO {

	private Integer functionCode;

	private String petQuestion;

	private String userReply;

	/**
	 * 用户提供内容后，宠物的友好回复
	 */
	private List<String> petReply = new ArrayList<>();

	/**
	 * 宠物将内容分享给他人的友好前缀
	 */
	private List<String> sharePrefix = new ArrayList<>();

	public String getPetQuestion() {
		return petQuestion;
	}

	public void setPetQuestion(String petQuestion) {
		this.petQuestion = petQuestion;
	}

	public String getUserReply() {
		return userReply;
	}

	public void setUserReply(String userReply) {
		this.userReply = userReply;
	}

	public List<String> getPetReply() {
		return petReply;
	}

	public void setPetReply(List<String> petReply) {
		this.petReply = petReply;
	}

	public List<String> getSharePrefix() {
		return sharePrefix;
	}

	public void setSharePrefix(List<String> sharePrefix) {
		this.sharePrefix = sharePrefix;
	}

	public Integer getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(Integer functionCode) {
		this.functionCode = functionCode;
	}

}
