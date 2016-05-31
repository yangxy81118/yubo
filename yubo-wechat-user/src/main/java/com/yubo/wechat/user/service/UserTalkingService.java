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
	 * 存储一次简单对话
	 * 
	 * @param userTalkVO
	 */
	public void saveTalk(UserTalkVO userTalkVO) {

		TalkHistory record = new TalkHistory();
		record.setUserId(userTalkVO.getUserId());
		record.setPetId(userTalkVO.getPetId());
		record.setLastTalkUserSaid(userTalkVO.getUserSaid());
		record.setLastTalkPetSaid(userTalkVO.getPetSaid());
		record.setLastTalkFuncCode(userTalkVO.getTalkFuncCode());
		record.setLastTalkTime(userTalkVO.getLastTalkTime());
		
		//功能性聊天，加入一个状态位
		if(userTalkVO.getTalkFuncCode()!=0){
			record.setLastTalkState(1);
		}
		historyMapper.insertSelective(record);
	}
	
	/**
	 * 将用户的功能性输入存储
	 * 
	 * @param userTalkVO
	 */
	public void updateTalk(UserTalkVO userTalkVO) {
		TalkHistory record = new TalkHistory();
		record.setTalkId(userTalkVO.getTalkId());
		record.setLastTalkUserSaid(userTalkVO.getUserSaid());
		record.setLastTalkState(2);
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
		if(!EmptyChecker.isEmpty(history)){
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
	
	@Autowired
	TalkHistoryMapper historyMapper;
	
	private static final Logger logger = LoggerFactory
			.getLogger(UserTalkingService.class);


}
