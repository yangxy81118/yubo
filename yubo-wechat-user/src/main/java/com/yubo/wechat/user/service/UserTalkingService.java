package com.yubo.wechat.user.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.user.dao.TalkHistoryMapper;
import com.yubo.wechat.user.dao.pojo.TalkHistory;
import com.yubo.wechat.user.vo.UserTalkVO;

/**
 * 用户交流相关服务
 * 
 * @author yangxy8
 *
 */
@Service
public class UserTalkingService {

	/**
	 * 存储一次对话
	 * 
	 * @param userTalkVO
	 */
	public void saveSimpleTalk(UserTalkVO userTalkVO) {

		TalkHistory record = new TalkHistory();
		record.setUserId(userTalkVO.getUserId());
		record.setPetId(userTalkVO.getPetId());
		record.setLastTalkUserSaid(userTalkVO.getUserSaid());
		record.setLastTalkPetSaid(userTalkVO.getPetSaid());
		record.setLastTalkFuncCode(userTalkVO.getTalkFuncCode());

		// 功能性聊天，加入一个状态位
		if (userTalkVO.getTalkFuncCode() != 0) {
			record.setLastTalkState(1);
		}
		historyMapper.insertSelective(record);
	}

	/**
	 * 将用户的功能性输入存储
	 * 
	 * @param userTalkVO
	 */
	public void addUserTalk(UserTalkVO userTalkVO) {
		TalkHistory record = new TalkHistory();
		record.setTalkId(userTalkVO.getTalkId());
		record.setLastTalkUserSaid(userTalkVO.getUserSaid());
		record.setLastTalkState(2);
		record.setLastTalkFuncCode(userTalkVO.getTalkFuncCode());
		record.setUserId(userTalkVO.getUserId());
		record.setPetId(userTalkVO.getPetId());
		historyMapper.insertSelective(record);
	}

	/**
	 * 本次功能性输入用户无内容回复，关闭
	 * 
	 * @param userTalkVO
	 */
	public void closeTalk(UserTalkVO userTalkVO) {
		TalkHistory record = new TalkHistory();
		record.setTalkId(userTalkVO.getTalkId());
		record.setLastTalkState(0);
		historyMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 获取用户的功能性对话
	 * 
	 * @param userId
	 * @param petId
	 * @return
	 */
	public UserTalkVO getFunctionTalk(int userId, int petId) {

		TalkHistory param = new TalkHistory();
		param.setPetId(petId);
		param.setUserId(userId);
		param.setLastTalkState(1);
		List<TalkHistory> history = historyMapper.selectBySelective(param);
		if (!EmptyChecker.isEmpty(history)) {
			TalkHistory historyItem = history.get(0);
			UserTalkVO vo = new UserTalkVO();
			vo.setUserId(userId);
			vo.setPetId(petId);
			vo.setPetSaid(historyItem.getLastTalkPetSaid());
			vo.setTalkFuncCode(historyItem.getLastTalkFuncCode());
			vo.setTalkId(historyItem.getTalkId());
			return vo;
		}

		return null;
	}

	/**
	 * 用户拒绝回答本次功能性问题
	 * 
	 * @param content
	 * @return
	 */
	public boolean userRejection(String content) {
		content = content.trim();
		for (int i = 0; i < rejectionWords.length; i++) {
			if(content.equals(rejectionWords[i])){
				return true;
			}
		}
		return false;
	}

	@Autowired
	TalkHistoryMapper historyMapper;

	private static final Logger logger = LoggerFactory
			.getLogger(UserTalkingService.class);

	private static final String[] rejectionWords = { "没有", "算了", "下次", "下次吧",
			"不知", "不知道", "不了解", "没", "NO", "no", "不感兴趣" };

}
