package com.yubo.wechat.api.xml.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 文本消息
 * 
 * @author young.jason
 *
 */
@XmlRootElement(name="xml")
public class TextMsgRequest extends MsgRequest {

	@XmlElement(name="Content")
	private String content;
	
}
