package com.yubo.wechat.api.xml.response;

import javax.xml.bind.annotation.XmlElement;

import com.yubo.wechat.api.xml.response.BaseXML;

/**
 * 微信消息/事件推送XML模型基础类
 * 
 * @author young.jason
 *
 */
public class MsgResponse extends BaseXML {

	private String toUserName;

	private String fromUserName;

	@XmlElement(name = "CreateTime")
	private Long createTime;

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@XmlElement(name = "ToUserName")
	public String getToUserName() {
		return wrapWithCDATA(toUserName);
	}

	@XmlElement(name = "FromUserName")
	public String getFromUserName() {
		return wrapWithCDATA(fromUserName);
	}

}
