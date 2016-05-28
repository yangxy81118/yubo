package com.yubo.wechat.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.EventMsgRequest;
import com.yubo.wechat.api.xml.request.TextMsgRequest;
import com.yubo.wechat.api.xml.response.ArticleItem;
import com.yubo.wechat.api.xml.response.ViewResponse;
import com.yubo.wechat.vote.service.vo.UserVoteVO;

/**
 * 我和投票业务处理<br/>
 * 用户点击“问答”按钮，系统返回查看我的问答图文消息
 * 
 * @author young.jason
 *
 */
@Service
public class MyVoteService implements MessageHandler {

	public MsgHandlerResult execute(MsgInputParam param) {

		try {
			EventMsgRequest request = XMLHelper.parseXml(param.requestBody,
					EventMsgRequest.class);
			
			return buildResult(request,param.userId);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 构建结果
	 * TODO URL等常量配置化
	 * @param request
	 * @param feedbackText
	 * @return
	 * @throws JAXBException
	 */
	private MsgHandlerResult buildResult(EventMsgRequest request,Integer userId)
			throws JAXBException {

		ViewResponse response = new ViewResponse();
		response.setCreateTime(System.currentTimeMillis());
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());
		response.setArticleCount(1);

		List<ArticleItem> articles = new ArrayList<>();
		ArticleItem item = new ArticleItem();
		item.setDescription("YUBO每天都有各种各样的问题，多谢大家帮忙解答~");
		item.setTitle("问答记录");
		item.setUrl("http://www.yubo.space/vote/list?userId="+userId);
		item.setPicUrl(DEFAULT_FEEDBACK_PIC_URL);
		articles.add(item);
		response.setItems(articles);

		MsgHandlerResult result = new MsgHandlerResult();
		result.setXmlResponse(XMLHelper.buildXMLStr(response,
				ViewResponse.class));
		return result;
	}

	private static final String DEFAULT_FEEDBACK_PIC_URL = "http://pic.58pic.com/58pic/11/38/72/858PICc58PICbTC.jpg";

	private static final Logger logger = LoggerFactory
			.getLogger(MyVoteService.class);

}
