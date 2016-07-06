package com.yubo.wechat.api.xml.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.yubo.wechat.api.xml.response.BaseXML;

/**
 * 微信消息数据共用类
 * @author yangxy8
 *
 */
@XmlRootElement(name="xml")
public class WeChatRequest extends BaseXML {

	@XmlElement(name = "ToUserName")
	private String toUserName;

	@XmlElement(name = "FromUserName")
	private String fromUserName;

	@XmlElement(name = "CreateTime")
	private Long createTime;

	@XmlElement(name = "MsgId")
	private String msgId;

	@XmlElement(name = "MsgType")
	private String msgType;

	@XmlElement(name = "Content")
	private String content;

	@XmlElement(name = "Event")
	private String event;

	@XmlElement(name = "EventKey")
	private String eventKey;

	public String getEvent() {
		return event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public String getContent() {
		return content;
	}

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
