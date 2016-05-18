package com.yubo.wechat.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.user.dao.UserPetFavorMapper;

/**
 * 亲密度服务
 * 
 * @author young.jason
 *
 */
@Service
public class UserPetFavorService {

	private static final int FAVOR_MAX = 3;

	@Autowired
	UserPetFavorMapper userPetFavorMapper;

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public int favorIncrease(int userId, int petId) {

		// 暂时处理方式，随机产生一个point
		int favorIncreasePoint = (int) (Math.random() * FAVOR_MAX);
		
		// 加入到数据库中
		

		return favorIncreasePoint;
	}

}
