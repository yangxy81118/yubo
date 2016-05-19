package com.yubo.wechat.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.user.dao.TalkHistoryMapper;
import com.yubo.wechat.user.dao.UserBaseMapper;
import com.yubo.wechat.user.dao.pojo.TalkHistory;
import com.yubo.wechat.user.dao.pojo.UserBase;
import com.yubo.wechat.user.vo.SimpleTalkVO;
import com.yubo.wechat.user.vo.UserVO;

/**
 * 用户常规服务
 * 
 * @author young.jason
 *
 */
@Service
public class UserService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserService.class);

	@Autowired
	UserBaseMapper userBaseMapper;

	@Autowired
	TalkHistoryMapper historyMapper;
	
	@Autowired
	User4WechatMapping user4WechatMapping;

	/**
	 * 通过微信ID获取本系统中对应用户ID<br/>
	 * 如果用户是第一次登录，则本方法会协助先生成一个UserId，然后返回
	 * 
	 * @param wechatID
	 * @return
	 */
	public int getUserIdByWeChatId(String wechatId) {
		
		Integer userId = user4WechatMapping.getUserId(wechatId);
		if(userId!=null){
			return userId;
		}else{
			
			//如果缓存中没有，再去数据库查一次
			Map<String, Object> param = new HashMap<>();
			param.put("wechatId", wechatId);
			param.put("rowCount", 1);
			param.put("startRow", 0);

			List<UserBase> list = userBaseMapper.selectByParam(param);

			if (list.size() > 0) {
				userId = list.get(0).getUserId();
				user4WechatMapping.addNewUser(wechatId, userId);
				return userId;
			} else {
				UserBase record = new UserBase();
				record.setWechatId(wechatId);
				record.setPetId(1); // 目前宠物默认都是1
				userBaseMapper.insertSelective(record);
				logger.info("微信用户{}第一次访问校宠系统，分配用户ID为{}", wechatId,
						record.getUserId());
				user4WechatMapping.addNewUser(wechatId, record.getUserId());
				return record.getUserId();
			}
		}
	}

	/**
	 * 根据用户ID获取用户的集合信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserVO getUserVOByUserId(int userId) {
		UserBase userBase = userBaseMapper.selectByPrimaryKey(userId);
		UserVO vo = new UserVO();
		vo.setUserId(userId);
		vo.setWechatId(userBase.getWechatId());
		return vo;
	}

	/**
	 * 存储一次简单对话
	 * 
	 * @param simpleTalkVO
	 */
	public void saveSimpleTalk(SimpleTalkVO simpleTalkVO) {

		TalkHistory record = new TalkHistory();
		record.setUserId(simpleTalkVO.getUserId());
		record.setPetId(simpleTalkVO.getPetId());
		record.setLastTalkUserSaid(simpleTalkVO.getUserSaid());
		record.setLastTalkPetSaid(simpleTalkVO.getPetSaid());
		record.setLastTalkFuncCode(simpleTalkVO.getTalkFuncCode());
		record.setLastTalkTime(simpleTalkVO.getLastTalkTime());
		historyMapper.insertSelective(record);

	}

}
