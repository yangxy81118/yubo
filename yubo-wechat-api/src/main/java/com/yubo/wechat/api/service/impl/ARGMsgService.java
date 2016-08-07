package com.yubo.wechat.api.service.impl;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgContextParam;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.WeChatRequest;
import com.yubo.wechat.user.service.ArgService;
import com.yubo.wechat.user.service.UserAuthorizeService;
import com.yubo.wechat.user.vo.ArgPlayResponseVO;
import com.yubo.wechat.user.vo.ArgPlayVO;
import com.yubo.wechat.user.vo.AuthorizeVO;

/**
 * ARG文本消息处理
 * 
 * @author young.jason
 *
 */
@Service
public class ARGMsgService implements MessageHandler {

	public MsgHandlerResult execute(MsgContextParam param) throws JAXBException {

		ArgPlayVO playProgressVO = argService.getUserProgress(param.userId);
		
		if (playProgressVO.getCurrentStep().intValue() == 0) {
			WeChatRequest request = param.request;
			String userContent = request.getContent();

			// 如果成功，将用户的进度改为1，加入认证信息，并获取第一道题的问题
			if (authCodeCorrect(userContent)) {
				playProgressVO.setCurrentStep(1);
				AuthorizeVO authVO = userAuthorizeService
						.getAuthorizeByCode(userContent);
				playProgressVO.setAuthIdentificationId(authVO.getIdentiId());
				argService.updateUserArgProgress(playProgressVO);
				return XMLHelper.buildTextResponse(argService.getStepQuestion(1),
						param.request);
				
			} else {
				return XMLHelper.buildTextResponse("无效的邀请码，请从新输入~",
						param.request);
			}

		} else {
			// 如果当前进度不是0，则直接调用play
			ArgPlayVO argPlayVO = new ArgPlayVO();
			argPlayVO.setUserId(param.userId);
			argPlayVO.setUserMessage(param.request.getContent());
			ArgPlayResponseVO response = argService.play(argPlayVO);
			return XMLHelper.buildTextResponse(response.getResponseMessage(),
					param.request);
		}

	}

	private boolean authCodeCorrect(String userContent) {
		return userAuthorizeService.getAuthorizeByCode(userContent)!=null;
	}

	private static final Logger logger = LoggerFactory
			.getLogger(ARGMsgService.class);

	@Autowired
	ArgService argService;

	@Autowired
	UserAuthorizeService userAuthorizeService;
}
