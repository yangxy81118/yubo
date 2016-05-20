package com.yubo.wechat.api.service.helper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.TextMsgRequest;
import com.yubo.wechat.api.xml.response.ArticleItem;
import com.yubo.wechat.api.xml.response.ViewResponse;
import com.yubo.wechat.vote.service.VoteService;
import com.yubo.wechat.vote.service.vo.VoteVO;

/**
 * 委派处理投票相关事务
 * 
 * @author young.jason
 *
 */
@Component
public class VoteHelper {

	/**
	 * 是否是投票
	 * 
	 * @param content
	 * @return
	 */
	public boolean isVoteAnswer(String content) {
		return voteService.getVoteIdByWord(content) != null;
	}

	/**
	 * 执行投票流程
	 * 
	 * @param param
	 * @param request
	 * @return
	 * @throws JAXBException 
	 */
	public MsgHandlerResult execute(MsgInputParam param, TextMsgRequest request) throws JAXBException {

		String currentAnswer = request.getContent().trim();
		Long voteId = voteService.getVoteIdByWord(currentAnswer);
		VoteVO answerResult  = answer(voteId, param.userId, currentAnswer);
		return buildResult(request,answerResult);
	}
	
	/**
	 * 构建结果
	 * 
	 * @param request
	 * @param feedbackText
	 * @return
	 * @throws JAXBException
	 */
	private MsgHandlerResult buildResult(TextMsgRequest request,VoteVO answerResult)
			throws JAXBException {

		ViewResponse response = new ViewResponse();
		response.setCreateTime(System.currentTimeMillis());
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());
		response.setArticleCount(1);

		List<ArticleItem> articles = new ArrayList<>();
		ArticleItem item = new ArticleItem();
		item.setDescription(answerResult.getFeedBackText());
		item.setTitle("谢谢回答");
		item.setUrl("http://m.yunhou.com");
		item.setPicUrl(answerResult.getFeedBackPicUrl());
		articles.add(item);
		response.setItems(articles);

		MsgHandlerResult result = new MsgHandlerResult();
		result.setXmlResponse(XMLHelper.buildXMLStr(response,
				ViewResponse.class));
		return result;
	}

	private VoteVO answer(Long voteId, int userId, String currentAnswer) {
		VoteVO answerParam = new VoteVO();
		answerParam.setVoteId(voteId);
		answerParam.setUserId(userId);
		answerParam.setCurrentAnswer(currentAnswer);
		VoteVO answerResult = voteService.vote(answerParam);
		return answerResult;
	}

	@Autowired
	VoteService voteService;

}
