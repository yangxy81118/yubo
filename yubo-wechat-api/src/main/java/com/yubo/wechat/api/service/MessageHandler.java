package com.yubo.wechat.api.service;

import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;

public interface MessageHandler {
	
	/**
	 * 对业务进行处理
	 * 
	 * @param requestBody 微信推送请求内容
	 * @return
	 */
	public MsgHandlerResult execute(MsgInputParam inputParam);
	
}
