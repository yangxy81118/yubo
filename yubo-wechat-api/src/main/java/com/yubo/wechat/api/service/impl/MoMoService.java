package com.yubo.wechat.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.EventMsgRequest;
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.content.service.ReplyService;
import com.yubo.wechat.user.service.UserPetFavorService;
import com.yubo.wechat.user.service.UserService;
import com.yubo.wechat.user.vo.UserVO;

/**
 * 摸Mo业务处理
 * @author young.jason
 *
 */
@Service
public class MoMoService implements MessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(MoMoService.class);
	
	@Autowired
	ReplyService replyService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserPetFavorService userPetFavorService;
	
	public MsgHandlerResult execute(MsgInputParam param) {

		logger.info("摸Mo业务处理");
		
		try {
			EventMsgRequest request = XMLHelper.parseXml(param.requestBody, EventMsgRequest.class);
			
			TextResponse response = new TextResponse();
			response.setContent(buildContent(param));
			response.setCreateTime(System.currentTimeMillis());
			response.setFromUserName(request.getToUserName());
			response.setToUserName(request.getFromUserName());
			
			MsgHandlerResult result = new MsgHandlerResult();
			result.setXmlResponse(XMLHelper.buildXMLStr(response, TextResponse.class));
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 根据目前的时间段，获取一个回复
		// 若回复是一个非命令回复，将回复存储到Redis中，并定时15分钟的有效时间
		// key构建规则：simpleTalk.${petId}.${userId}，value为宠物的话
		// 若回复是一个命令回复，则将回复存储到MySQL talk_history中，永久等待用户下次回复（暂定永久）
		
		
		return null;
	}

	/**
	 * 构建内容
	 * @param param 
	 * @return
	 */
	private String buildContent(MsgInputParam param) {

		StringBuffer content = new StringBuffer("");
		
		//主要回复内容
		String mainText = replyService.replyText(null);
		content.append(mainText);
		
		UserVO userVO = userService.getUserVOByUserId(param.userId);
		
		//添加亲密度
		int favorPoint = userPetFavorService.favorIncrease(param.userId,1);
		if(favorPoint>0){
			content.append("【YUBO亲密度+").append(favorPoint).append("】");
		}
		
		return content.toString();
	}

}
