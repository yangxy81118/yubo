package com.yubo.wechat.content.vo;

/**
 * Message of User coming from WeChat<br/>
 * For creating specific message,this ValueObject contains not only user_ID , but also message content. 
 * @author young.jason
 *
 */
public class UserMessageInfo {

	private Integer userId;
	
	private Double userFavorPoint;
	
	private String messageContent;
}
