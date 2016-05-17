package com.yubo.wechat.api.xml.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 点击事件
 * 
 * @author young.jason
 *
 */
@XmlRootElement(name="xml")
public class EventMsgRequest extends MsgRequest {

	@XmlElement(name="Event")
	private String event;
	
	@XmlElement(name="EventKey")
	private String eventKey;

	public String getEvent() {
		return event;
	}

	public String getEventKey() {
		return eventKey;
	}
	
	
	
}
