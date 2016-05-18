package com.yubo.wechat.user.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.user.dao.UserBaseMapper;
import com.yubo.wechat.user.dao.pojo.UserBase;

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

	/**
	 * 通过微信ID获取本系统中对应用户ID<br/>
	 * 如果用户是第一次登录，则本方法会协助先生成一个UserId，然后返回
	 * 
	 * @param wechatID
	 * @return
	 */
	public int getUserIdByWeChatId(String wechatID) {

		Integer userId = getUserIdFromDB(wechatID);
		return userId;
	}

	private Integer getUserIdFromDB(String wechatID) {

		Integer userId = null;
		
		UserBase param = new UserBase();
		param.setWechatId(wechatID);
		List<UserBase> list = userBaseMapper.selectByParam(param);
		
		if(list.size() > 0){
			return list.get(0).getUserId();
		}else{
			UserBase record = new UserBase();
			record.setWechatId(wechatID);
			userBaseMapper.insertSelective(record);
			logger.info("微信用户{}第一次访问校宠系统，分配用户ID为{}",wechatID,record.getUserId());
			return record.getUserId();
		}
	}
}
