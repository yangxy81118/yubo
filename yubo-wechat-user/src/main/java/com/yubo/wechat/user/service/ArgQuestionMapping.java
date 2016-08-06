package com.yubo.wechat.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.support.PageUtil;
import com.yubo.wechat.user.dao.ArgQuestionMapper;
import com.yubo.wechat.user.dao.pojo.ArgQuestion;
import com.yubo.wechat.user.dao.pojo.UserBase;
import com.yubo.wechat.user.vo.ArgQuestionVO;

/**
 * ARG问答缓存
 * @author yangxy8
 *
 */
@Service
public class ArgQuestionMapping {

	private Map<Integer, ArgQuestionVO> questionMap = new HashMap<>();

	@PostConstruct
	public void loadMapping() {
		
		logger.info("开始加载ARG问题映射");
		
		int totalCount = getCount();
		int pageCount = PageUtil.pageCount(totalCount, PAGE_SIZE);
		for (int i = 0; i < pageCount; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("startRow", i * PAGE_SIZE);
			param.put("rowCount", PAGE_SIZE);
			param.put("argTopicId", 1);
			List<ArgQuestion> list = argQuestionMapper.selectByParam(param);
			for (ArgQuestion argQuestion : list) {
				ArgQuestionVO serviceVO = buildServiceVO(argQuestion);
				questionMap.put(argQuestion.getArgStep(), serviceVO);
			}
		}
		logger.info("开始加载微信ID与用户ID的映射加载完毕，一共{}条数据",questionMap.size());
	}
	
	/**
	 * 根据step获取
	 * @param topicId
	 * @param stepId
	 * @return
	 */
	public ArgQuestionVO getQuestionByStep(int topicId,int stepId){
		return questionMap.get(stepId);
	}
	
	
	private ArgQuestionVO buildServiceVO(ArgQuestion argQuestion) {
		ArgQuestionVO bo = new ArgQuestionVO();
		bo.setAnswer(argQuestion.getAnswser());
		bo.setQuestion(argQuestion.getQuestion());
		bo.setWrongReply(argQuestion.getReplyForWrong());
		return bo;
	}

	private int getCount() {
		ArgQuestion param = new ArgQuestion();
		param.setArgTopicId(1);
		return argQuestionMapper.countByParam(param);
	}
	
	private static final int PAGE_SIZE = 30;
	
	@Autowired
	ArgQuestionMapper argQuestionMapper;
	
	private static final Logger logger = LoggerFactory
			.getLogger(ArgQuestionMapping.class);
}
