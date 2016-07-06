package com.yubo.wechat.pet.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yubo.wechat.pet.dao.PetBaseMapper;
import com.yubo.wechat.pet.dao.pojo.PetBase;

/**
 * 宠物状态监视器
 * @author yangxy8
 *
 */
@Component
public class PetStateListener {

	private Map<Integer,Double> petStateMapping = new HashMap<>();
	
	
	@PostConstruct
	public void init(){
		PetBase pet = petBaseMapper.selectByPrimaryKey(1);
		petStateMapping.put(1, pet.getPetLevel());
	}
	
	public Double getPetLevel(Integer petId){
		return petStateMapping.get(petId);
	}
	
	@Autowired
	PetBaseMapper petBaseMapper;


	public void addLevel(int petId, double growUpPoint) {
		Double petLevel = petStateMapping.get(petId);
		if(petLevel==null){
			petStateMapping.put(petId, growUpPoint);
		}
	}

	
}
