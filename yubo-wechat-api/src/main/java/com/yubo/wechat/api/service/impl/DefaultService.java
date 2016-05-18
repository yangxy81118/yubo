package com.yubo.wechat.api.service.impl;

import org.springframework.stereotype.Service;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.response.TextResponse;

/**
 * 异常缺省处理
 * @author young.jason
 *
 */
@Service
public class DefaultService implements MessageHandler {

	public MsgHandlerResult execute(MsgInputParam param) {

//		MsgHandlerResult result = new MsgHandlerResult();
		
		return null;
	}

}
