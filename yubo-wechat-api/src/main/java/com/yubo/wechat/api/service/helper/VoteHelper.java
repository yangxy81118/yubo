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
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.api.xml.response.ViewResponse;
import com.yubo.wechat.user.service.UserPetFavorService;
import com.yubo.wechat.user.service.UserService;
import com.yubo.wechat.vote.service.VoteService;
import com.yubo.wechat.vote.service.vo.AnswerEntry;
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
	 * TODO 只是用来测试
	 * 
	 * @param request
	 * @return
	 * @throws JAXBException 
	 */
	public MsgHandlerResult testForQuestion(TextMsgRequest request) throws JAXBException {

		String word = request.getContent().trim();
		if(!word.equals("今天的问题")){
			return null;
		}
		
		Long voteId = voteService.getFirstVoteId();
		VoteVO vo = voteService.getVoteVOByVoteId(voteId);
		
		TextResponse response = new TextResponse();
		String content = "YUBO今天的问题，希望听听你的想法~\n(请回复括号中的关键字)\n\n"+vo.getVoteQuestion()+"\n";
		
		List<AnswerEntry> as =  vo.getVoteAnswers();
		for (AnswerEntry answerEntry : as) {
			content += answerEntry.getAnswerDiscription() + "【"+answerEntry.getAnswerKey()+"】\n";
		}
		
		response.setContent(content);
		response.setCreateTime(System.currentTimeMillis());
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());

		MsgHandlerResult result = new MsgHandlerResult();
		result.setXmlResponse(XMLHelper.buildXMLStr(response,
				TextResponse.class));
		return result;
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
		petFavorService.addFavor(param.userId, param.petId, 1);
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
		item.setUrl("http://www.yubo.space/pet/level");
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
	
	@Autowired
	UserPetFavorService petFavorService;

}
