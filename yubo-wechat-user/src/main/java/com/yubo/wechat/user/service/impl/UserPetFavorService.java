package com.yubo.wechat.user.service.impl;

import org.springframework.stereotype.Service;

/**
 * 亲密度服务
 * @author young.jason
 *
 */
@Service
public class UserPetFavorService {

	private static final int FAVOR_MAX = 3;
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public int favorIncrease(String userId){
		int favorIncreasePoint = (int)(Math.random()*FAVOR_MAX);
		return favorIncreasePoint;
	}
	
}
