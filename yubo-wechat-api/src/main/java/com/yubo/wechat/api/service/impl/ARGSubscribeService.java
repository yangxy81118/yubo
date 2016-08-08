package com.yubo.wechat.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgContextParam;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.user.service.ArgService;
import com.yubo.wechat.user.vo.ArgPlayVO;

/**
 * 摸Mo业务处理
 * 
 * @author young.jason
 *
 */
@Service
public class ARGSubscribeService implements MessageHandler {

	public MsgHandlerResult execute(MsgContextParam param) throws Exception {
		
		//如果该用户之前已经关注过并且走到了对应步骤，则直接返回对应步骤的话语
		ArgPlayVO userCurrentProgress = argService.getUserProgress(param.userId);
		if(userCurrentProgress == null){
			ArgPlayVO argPlayVO = new ArgPlayVO();
			argPlayVO.setGameTopicId(1);
			argPlayVO.setUserId(param.userId);
			argService.register(argPlayVO);
			return XMLHelper.buildTextResponse(WELCOME_MESSAGE, param.request);
		}else{
			int currentStep = userCurrentProgress.getCurrentStep().intValue();
			if(currentStep==0){
				return XMLHelper.buildTextResponse(WELCOME_MESSAGE, param.request);
			}else{
				return XMLHelper.buildTextResponse(argService.getStepQuestion(currentStep), param.request);
			}
		}
		
		
		
	}

	private static final Logger logger = LoggerFactory
			.getLogger(ARGSubscribeService.class);

	@Autowired
	ArgService argService;
	

	private static final String WELCOME_MESSAGE = "你好！欢迎参加挑战！请输入邀请码，我们确认后挑战即时开始！";
}
