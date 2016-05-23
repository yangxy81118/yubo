package com.yubo.wechat.pet.service;

import org.springframework.stereotype.Service;

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
		
	}

	/**
	 * 检查宠物是否还在蛋壳状态
	 * @param petId
	 * @return
	 */
	public boolean stillInEgg(int petId) {
		return true;
	}

}
