package com.yubo.wechat.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.api.PropertiesHolder;
import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgContextParam;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.xml.XMLHelper;

/**
 * 我和投票业务处理<br/>
 * 用户点击“问答”按钮，系统返回查看我的问答图文消息
 * 
 * @author young.jason
 *
 */
@Service
public class MyVoteService implements MessageHandler {

	public MsgHandlerResult execute(MsgContextParam param) throws Exception  {

		String redirectUrl = propertiesHolder.getYuboUrl() + "/vote/list?userId="
				+ param.userId;
		String title = "问答记录";
		String description = "YUBO每天都有各种各样的问题，多谢大家帮忙解答~";
		return XMLHelper.buildSingleViewResponse(param.request, description,
				title, redirectUrl, DEFAULT_FEEDBACK_PIC_URL);
		
	}

	private static final String DEFAULT_FEEDBACK_PIC_URL = "http://pic.58pic.com/58pic/11/38/72/858PICc58PICbTC.jpg";

	private static final Logger logger = LoggerFactory
			.getLogger(MyVoteService.class);
	
	@Autowired
	PropertiesHolder propertiesHolder;

}
