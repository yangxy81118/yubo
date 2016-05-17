package com.yubo.wechat.api.xml.request;

import javax.xml.bind.annotation.XmlElement;

import com.yubo.wechat.api.xml.response.BaseXML;

/**
 * 微信消息/事件推送XML模型基础类
 * 
 * @author young.jason
 *
 */
public class MsgRequest extends BaseXML {

	@XmlElement(name="ToUserName")
	private String toUserName;
	
	@XmlElement(name="FromUserName")
	private String fromUserName;
	
	@XmlElement(name="CreateTime")
	private Long createTime;
	
	@XmlElement(name="MsgId")
	private String msgId;
	
	@XmlElement(name="MsgType")
	private String msgType;

	public String getToUserName() {
		return toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public String getMsgId() {
		return msgId;
	}

	public String getMsgType() {
		return msgType;
	}

	
}
