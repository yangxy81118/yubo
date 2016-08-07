package com.yubo.wechat.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.user.dao.ArgUserProgressMapper;
import com.yubo.wechat.user.dao.pojo.ArgUserProgress;
import com.yubo.wechat.user.vo.ArgPlayResponseVO;
import com.yubo.wechat.user.vo.ArgPlayVO;
import com.yubo.wechat.user.vo.ArgQuestionVO;

/**
 * ARG游戏相关业务
 * 
 * @author yangxy8
 *
 */
@Service
public class ArgService {

	/**
	 * 用户第一次参与游戏，记录用户的游戏进度
	 * 
	 * @param argPlayVO
	 */
	public void register(ArgPlayVO argPlayVO) {

		ArgUserProgress record = new ArgUserProgress();
		record.setUserId(argPlayVO.getUserId());
		record.setArgTopicId(argPlayVO.getGameTopicId());
		record.setArgStep(0);
		argUserProgressMapper.insertSelective(record);
	};

	public ArgPlayVO getUserProgress(int userId) {

		// 根据userId获取step
		ArgUserProgress progress = getArgUserProgress(userId);

		ArgPlayVO playVO = new ArgPlayVO();
		playVO.setCurrentStep(progress.getArgStep());
		playVO.setUserId(userId);
		playVO.setUserProgressId(progress.getArgUserProgressId());
		return playVO;

	}
	
	public String getStepQuestion(Integer argQuestionId){
		return argQuestionMapping.getQuestionByStep(1, argQuestionId).getQuestion();
	}

	public void updateUserArgProgress(ArgPlayVO argPlayVO) {
		ArgUserProgress progress = new ArgUserProgress();
		progress.setArgUserProgressId(argPlayVO.getAuthIdentificationId());
		progress.setUserIdentificationId(argPlayVO.getAuthIdentificationId());
		progress.setArgStep(argPlayVO.getCurrentStep());
		argUserProgressMapper.updateByPrimaryKeySelective(progress);
	}

	private ArgUserProgress getArgUserProgress(int userId) {
		// 根据userId获取step
		ArgUserProgress record = new ArgUserProgress();
		record.setUserId(userId);
		List<ArgUserProgress> userProgress = argUserProgressMapper
				.selectByParam(record);

		if (EmptyChecker.isEmpty(userProgress)) {
			throw new RuntimeException("用户没有进度数据");
		}

		ArgUserProgress progress = userProgress.get(0);
		return progress;
	}

	/**
	 * 游戏参与主业务，根据用户的回答是否正确
	 * 
	 * @param argPlayVO
	 * @return
	 */
	public ArgPlayResponseVO play(ArgPlayVO argPlayVO) {

		// 根据userId获取step
		ArgUserProgress progress = getArgUserProgress(argPlayVO.getUserId());

		// 根据step获取answer
		ArgQuestionVO userProgressQuestion = argQuestionMapping
				.getQuestionByStep(1, progress.getArgStep());
		String correctAnswer = userProgressQuestion.getAnswer();

		String responseMsg = null;
		if (correctAnswer.equals(argPlayVO.getUserMessage())) {
			responseMsg = answerCorrect(progress, userProgressQuestion);
		} else {
			responseMsg = answerWrong(userProgressQuestion);
		}

		ArgPlayResponseVO response = new ArgPlayResponseVO();
		response.setResponseMessage(responseMsg);
		return response;
	}

	private String answerWrong(ArgQuestionVO userProgressQuestion) {
		return userProgressQuestion.getWrongReply();
	}

	private String answerCorrect(ArgUserProgress progress,
			ArgQuestionVO userProgressQuestion) {

		if (progress.getArgStep().intValue() == MAX_STEP) {
			// TODO ARG，在这里可能得加上完成触发的事件
			return FINISH_RESPONSE;
		} else {
			int nextStep = progress.getArgStep() + 1;
			progress.setArgStep(nextStep);
			argUserProgressMapper.updateByPrimaryKeySelective(progress);

			ArgQuestionVO nextQuestion = argQuestionMapping.getQuestionByStep(
					1, nextStep);
			return nextQuestion.getQuestion();
		}

	}
	
	@Autowired
	ArgUserProgressMapper argUserProgressMapper;

	@Autowired
	ArgQuestionMapping argQuestionMapping;

	private static final int MAX_STEP = 10;

	private static final String FINISH_RESPONSE = "恭喜！顺利通关！抬头看看屏幕吧，礼物在等着你哟！";

}
