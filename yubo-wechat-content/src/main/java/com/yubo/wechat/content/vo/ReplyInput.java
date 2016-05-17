package com.yubo.wechat.content.vo;

/**
 * 进行回复业务的传入从参数
 * 
 * @author young.jason
 *
 */
public class ReplyInput {

	/**
	 * 微信用户OpenId
	 */
	private String wechatOpenId;

	/**
	 * 微信消息内容
	 */
	private String content;

	/**
	 * 本条消息创建时间
	 */
	private Long createTime;

	public String getWechatOpenId() {
		return wechatOpenId;
	}

	public void setWechatOpenId(String wechatOpenId) {
		this.wechatOpenId = wechatOpenId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
