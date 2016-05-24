package com.yubo.wechat.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.user.dao.UserPetFavorMapper;
import com.yubo.wechat.user.dao.pojo.UserPetFavor;

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
	 * 创建一个亲密度
	 * 
	 * @param userId
	 * @param petId
	 * @return
	 */
	public int getFavorPoint(int userId, int petId) {
		int favorIncreasePoint = (int) (Math.random() * FAVOR_MAX);
		return favorIncreasePoint;
	}
	
	
	public void addFavor(int userId, int petId, int favorIncreasePoint) {
		
		UserPetFavor record = new UserPetFavor();
		record.setUserId(userId);
		record.setPetId(petId);
		List<UserPetFavor> dbResult = userPetFavorMapper.selectByParam(record);
		
		if(dbResult == null || dbResult.size() == 0){
			record.setFavorPoint((double)favorIncreasePoint);
			userPetFavorMapper.insertSelective(record);
		}else{
			UserPetFavor previousFavor =  dbResult.get(0);
			UserPetFavor thisFavor = new UserPetFavor();
			thisFavor.setFavorPoint(favorIncreasePoint + previousFavor.getFavorPoint());
			thisFavor.setFavorId(previousFavor.getFavorId());
			userPetFavorMapper.updateByPrimaryKeySelective(thisFavor);
		}
		
	}

}
