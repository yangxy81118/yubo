package com.yubo.wechat.pet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.pet.dao.PetBaseMapper;
import com.yubo.wechat.pet.dao.pojo.PetBase;

/**
 * 宠物相关服务
 * 
 * @author young.jason
 *
 */
@Service
public class PetService {
	
	/**
	 * 宠物成长
	 * @param growUpPoint
	 */
	public void growUp(int petId,double growUpPoint){
		PetBase pet = new PetBase();
		pet.setPetId(petId);
		pet.setPetLevel(growUpPoint + pet.getPetLevel());
		petBaseMapper.updateByPrimaryKeySelective(pet);
		petStateListener.addLevel(petId,growUpPoint);
	}

	/**
	 * 检查宠物是否还在蛋壳状态
	 * @param petId
	 * @return
	 */
	public boolean stillInEgg(int petId) {
		return petStateListener.getPetLevel(1) < 1;
	}
	
	
	@Autowired
	PetBaseMapper petBaseMapper;
	
	@Autowired
	PetStateListener petStateListener;

}
