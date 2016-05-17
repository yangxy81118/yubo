package com.yubo.wechat.api.xml.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 文本回复
 * 
 * @author young.jason
 *
 */

@XmlRootElement(name = "xml")
public class TextResponse extends BaseXML {
	/*
	 * 
	 * <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[fromUser]]></FromUserName>
	 * <CreateTime>12345678</CreateTime> <MsgType><![CDATA[text]]></MsgType>
	 * <Content><![CDATA[你好]]></Content> </xml>
	 */

	private String toUserName;
	private String fromUserName;
	private Long createTime;
	private String content;

	@XmlElement(name = "ToUserName")
	public String getToUserName() {
		return wrapWithCDATA(toUserName);
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	@XmlElement(name = "FromUserName")
	public String getFromUserName() {
		return wrapWithCDATA(fromUserName);
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@XmlElement(name = "CreateTime")
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@XmlElement(name = "MsgType")
	public String getMsgType() {
		return wrapWithCDATA("text");
	}

	@XmlElement(name = "Content")
	public String getContent() {
		return wrapWithCDATA(content);
	}

	public void setContent(String content) {
		this.content = content;
	}

}
