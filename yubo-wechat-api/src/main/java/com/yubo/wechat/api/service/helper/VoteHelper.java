package com.yubo.wechat.api.service.helper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yubo.wechat.api.service.vo.MsgContextParam;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.WeChatRequest;
import com.yubo.wechat.api.xml.response.ArticleItem;
import com.yubo.wechat.api.xml.response.ViewResponse;
import com.yubo.wechat.user.service.UserPetFavorService;
import com.yubo.wechat.vote.service.VoteService;
import com.yubo.wechat.vote.service.vo.AnswerEntry;
import com.yubo.wechat.vote.service.vo.UserVoteVO;
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
	public MsgHandlerResult testForQuestion(WeChatRequest request) throws JAXBException {

		String word = request.getContent().trim();
		if(!word.equals("今天的问题")){
			return null;
		}
		
		Long voteId = voteService.getFirstVoteId();
		if(voteId==null){
			return XMLHelper.buildTextResponse("YUBO今天很懒，没什么问题，zzZZ...",request);
		}
		
		VoteVO vo = voteService.getVoteInfoByVoteId(voteId);
		
		String content = "YUBO今天的问题，希望听听你的想法~\n(请回复括号中的关键字)\n\n"+vo.getVoteQuestion()+"\n";
		List<AnswerEntry> as =  vo.getVoteAnswers();
		for (AnswerEntry answerEntry : as) {
			content += answerEntry.getAnswerDiscription() + "【"+answerEntry.getAnswerKey()+"】\n";
		}
		
		return XMLHelper.buildTextResponse(content,request);
	}

	/**
	 * 执行投票流程
	 * 
	 * @param param
	 * @param request
	 * @return
	 * @throws JAXBException 
	 */
	public MsgHandlerResult execute(MsgContextParam param, WeChatRequest request) throws JAXBException {

		String currentAnswer = request.getContent().trim();
		Long voteId = voteService.getVoteIdByWord(currentAnswer);
		UserVoteVO answerResult  = answer(voteId, param.userId, currentAnswer);
		petFavorService.addFavor(param.userId, param.petId, 1);
		String url = "http://www.yubo.space/vote/detail?userId="+answerResult.getUserId()+"&voteId="+answerResult.getVoteVO().getVoteId();
		return XMLHelper.buildSingleViewResponse(request, answerResult.getFeedBackText(), "谢谢回答", url, answerResult.getFeedBackPicUrl());
	}
	
	private UserVoteVO answer(Long voteId, int userId, String currentAnswer) {
		UserVoteVO answerParam = new UserVoteVO();
		VoteVO vote = new VoteVO();
		vote.setVoteId(voteId);
		answerParam.setVoteVO(vote);
		answerParam.setUserId(userId);
		answerParam.setCurrentAnswer(currentAnswer);
		UserVoteVO answerResult = voteService.vote(answerParam);
		return answerResult;
	}

	@Autowired
	VoteService voteService;
	
	@Autowired
	UserPetFavorService petFavorService;

}
