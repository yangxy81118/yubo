package com.yubo.wechat.api.service;

import com.yubo.wechat.api.service.vo.MsgContextParam;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;

public interface MessageHandler {
	
	/**
	 * 对业务进行处理
	 * 
	 * @param requestBody 微信推送请求内容
	 * @return
	 */
	public MsgHandlerResult execute(MsgContextParam contextParam) throws Exception;
	
}
