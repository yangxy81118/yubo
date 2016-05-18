package com.yubo.wechat.user.vo;

/**
 * 用户信息集合类
 * 
 * @author young.jason
 *
 */
public class UserVO {

	private int userId;
	private String wechatId;

	// private PetVO pet;
	// private SchoolVO school;
	// private IdentificationVO identification;
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

}
