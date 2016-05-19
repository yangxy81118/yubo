package com.yubo.wechat.user.vo;

import java.util.Date;

/**
 * 简单对话VO 
 * @author young.jason
 *
 */
public class SimpleTalkVO {

	private int userId;

	private int petId;

	private String userSaid;

	private String petSaid;

	private int talkFuncCode;

	private Date lastTalkTime;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public String getUserSaid() {
		return userSaid;
	}

	public void setUserSaid(String userSaid) {
		this.userSaid = userSaid;
	}

	public String getPetSaid() {
		return petSaid;
	}

	public void setPetSaid(String petSaid) {
		this.petSaid = petSaid;
	}

	public int getTalkFuncCode() {
		return talkFuncCode;
	}

	public void setTalkFuncCode(int talkFuncCode) {
		this.talkFuncCode = talkFuncCode;
	}

	public Date getLastTalkTime() {
		return lastTalkTime;
	}

	public void setLastTalkTime(Date lastTalkTime) {
		this.lastTalkTime = lastTalkTime;
	}

}
