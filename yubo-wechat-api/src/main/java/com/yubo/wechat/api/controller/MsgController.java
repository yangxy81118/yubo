package com.yubo.wechat.api.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yubo.wechat.api.WeChatEventType;
import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.impl.ARGMsgService;
import com.yubo.wechat.api.service.impl.ARGSubscribeService;
import com.yubo.wechat.api.service.impl.DefaultService;
import com.yubo.wechat.api.service.impl.MoMoService;
import com.yubo.wechat.api.service.impl.MyVoteService;
import com.yubo.wechat.api.service.impl.TextMsgService;
import com.yubo.wechat.api.service.vo.MsgContextParam;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.WeChatRequest;
import com.yubo.wechat.support.redis.RedisHandler;
import com.yubo.wechat.user.service.ArgService;
import com.yubo.wechat.user.service.UserService;

/**
 * 微信信息派发接口<br/>
 * 在公众号上，所有用户的交互（输入文字，点击按钮，语音等等），均会从此入口经过，至于跳转其他H5页面，与此为异步关系
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/msg")
public class MsgController extends BaseController {

	
	@RequestMapping("")
	public ModelAndView msgAction(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		request.setCharacterEncoding("UTF-8");
		String requestBody = buildRequestBody(request);
		
		WeChatRequest wechatRequest = XMLHelper.parseXml(requestBody,
				WeChatRequest.class);
		
		logger.info("Request:\n{}", requestBody);

		// 首先将微信号转换为用户ID，并准备参数
		MsgContextParam contextParam = buildContextParam(wechatRequest);

		// 根据用户触发类型进行具体业务处理
		MessageHandler messageHandler = new DefaultService();
		WeChatEventType eventType = checkEventType(requestBody);
		switch (eventType) {
		case CLICK_MOMO:
			messageHandler = moMoService;
			break;
		case TEXT:
//			messageHandler = textMsgService;
			messageHandler = argMsgService;
			break;
		case CLICK_MY_VOTE:
			messageHandler = myFavorService;
			break;
		case SUBSCRIBE:
			messageHandler = argSubscribeService;
		default:
			break;
		}

		MsgHandlerResult result = messageHandler.execute(contextParam);

		String xmlResponseStr = result.getXmlResponse();
		
		if (!StringUtils.isEmpty(xmlResponseStr)) {
			writeResponse(response, xmlResponseStr);
			return null;
		} else {
			return null;
		}
		
		
	}

	private MsgContextParam buildContextParam(WeChatRequest req) {
		String weChatId = req.getFromUserName();
		int userId = userService.getUserIdByWeChatId(weChatId);
		MsgContextParam contextParam = new MsgContextParam();
		contextParam.userId = userId;
		contextParam.wechatId = weChatId;
		contextParam.request = req;
		return contextParam;
	}

	private void writeResponse(HttpServletResponse response,
			String xmlResponseStr) throws IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(xmlResponseStr);
	}

	/**
	 * 检查事件类型，同时讲请求体也缓存起来
	 * 
	 * @param request
	 * @param requestBody
	 *            请求体存储到这里
	 * @return
	 * @throws IOException
	 */
	private WeChatEventType checkEventType(String requestBody)
			throws IOException {

		// TODO 这一段现在简单粗暴，之后需要严格调整
		if (requestBody.indexOf("<MsgType><![CDATA[text]]></MsgType>") > 0) {
			return WeChatEventType.TEXT;
		} else if (requestBody.indexOf("CLICK_MOMO") > 0) {
			return WeChatEventType.CLICK_MOMO;
		} else if (requestBody.indexOf("CLICK_MY_VOTE") > 0) {
			return WeChatEventType.CLICK_MY_VOTE;
		} else if (requestBody.contains("<![CDATA[subscribe]]>")){
			return WeChatEventType.SUBSCRIBE;
		}

		// 其余均认为未知
		return WeChatEventType.UNKNOWN;
	}

	@Autowired
	MoMoService moMoService;

	@Autowired
	TextMsgService textMsgService;

	@Autowired
	MyVoteService myFavorService;

	@Autowired
	UserService userService;

	@Autowired
	RedisHandler redisHandler;
	
	@Autowired
	ARGMsgService argMsgService;
	
	@Autowired
	ARGSubscribeService argSubscribeService;
	
	private static final Logger logger = LoggerFactory
			.getLogger(MsgController.class);

}
