package com.yubo.wechat.api.xml.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 纯粹用来获取用户微信ID
 * 
 * @author young.jason
 *
 */
@XmlRootElement(name = "xml")
public class UserWeChatIdHelper {

	@XmlElement(name = "FromUserName")
	private String fromUserName;

	public String getFromUserName() {
		return fromUserName;
	}

}
