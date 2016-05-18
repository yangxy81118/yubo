package com.yubo.wechat.api.controller;

import java.io.BufferedReader;
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
import com.yubo.wechat.api.service.impl.DefaultService;
import com.yubo.wechat.api.service.impl.MoMoService;
import com.yubo.wechat.api.service.impl.MyFavorService;
import com.yubo.wechat.api.service.impl.TextMsgService;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;

/**
 * 微信信息派发接口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/msg")
public class MsgController {

	private static final Logger logger = LoggerFactory
			.getLogger(MsgController.class);

	@RequestMapping("")
	public ModelAndView msgAction(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");
		String requestBody = buildRequestBody(request);
		logger.info("Request:\n{}", requestBody);

		//首先将微信号转换为用户ID
		String userId = "";
		
		// 首先判断用户触发类型
		WeChatEventType eventType = checkEventType(requestBody);

		MessageHandler messageHandler = new DefaultService();
		switch (eventType) {
		case CLICK_MOMO:
			messageHandler = moMoService;
			break;
		case TEXT:
			messageHandler = textMsgService;
			break;
		case CLICK_MY_FAVOR:
			messageHandler = myFavorService;
			break;
		default:
			break;
		}

		MsgHandlerResult result = messageHandler.execute(requestBody);

		String xmlResponseStr = result.getXmlResponse();
		if (!StringUtils.isEmpty(xmlResponseStr)) {
			writeResponse(response, xmlResponseStr);
			return null;
		} else {
			return null;
		}

		// 1-按钮事件--摸Mo
		// 根据目前的时间段，获取一个回复
		// 若回复是一个非命令回复，将回复存储到Redis中，并定时15分钟的有效时间
		// key构建规则：simpleTalk.${petId}.${userId}，value为宠物的话
		// 若回复是一个命令回复，则将回复存储到MySQL talk_history中，永久等待用户下次回复（暂定永久）

		// 2-文本回复
		// 2.1 - 激活码回复
		// 进入激活码验证流程
		// 验证成功，进行记录
		// 验证失败，反馈提示

		// 2.2 - 普通回复
		// 首先判断去Redis中进行查找，key为simpleTalk.${petId}.${userId}
		// 如果存在，则说明是普通立即回复，将该回复内容与redis对应value值存入到数据库，然后清除redis中该key
		// 如果不存在，则检查是否是命令回复，去检查数据库talk_history表
		// 若在talk_history中找到了，则说明是命令回复，将本次回复进行记录，进行对应的业务处理（学笑话，学外语等等）
		// ---------上面这两种情况宠物是否需要再进行回复？暂无内容设计，定为不回复

		// 如果也没有找到，那么说明最一般的用户主动发文本消息过来，根据时间阶段进行摸Mo回复

		// 3-我的亲密度回复

	}

	private String buildRequestBody(HttpServletRequest request)
			throws IOException {

		String requestBody = "";
		request.setCharacterEncoding("UTF-8");
		BufferedReader br = request.getReader();
		String inputLine;
		try {
			while ((inputLine = br.readLine()) != null) {
				requestBody += inputLine;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return requestBody;
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
		} else if (requestBody.indexOf("VIEW") > 0) {
			return WeChatEventType.CLICK_MY_FAVOR;
		}

		// 其余均认为未知
		return WeChatEventType.UNKNOWN;
	}
	
	@Autowired
	MoMoService moMoService;
	
	@Autowired
	TextMsgService textMsgService;
	
	@Autowired
	MyFavorService myFavorService;
}
