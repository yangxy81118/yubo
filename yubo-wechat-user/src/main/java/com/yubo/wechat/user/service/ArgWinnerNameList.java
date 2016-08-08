package com.yubo.wechat.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yubo.wechat.user.dao.ArgUserProgressMapper;
import com.yubo.wechat.user.dao.UserIdentificationMapper;
import com.yubo.wechat.user.dao.pojo.ArgUserProgress;

/**
 * 
 * 游戏胜利者名单
 * 
 * @author yangxy8
 *
 */
@Component
public class ArgWinnerNameList {

	private final List<String> winnerNameList = new ArrayList<>();
	
	public ArgWinnerNameList(){
		winnerNameList.add("Tom");
		winnerNameList.add("Jason");
		winnerNameList.add("关羽");
		winnerNameList.add("张飞");
	}
	
	public synchronized void addWinnerByUserId(String playerName){
		winnerNameList.add(playerName);
	}
	
	public synchronized List<String> getNameList(){
		return winnerNameList;
	}
	
	@Autowired
	UserIdentificationMapper userIdentificationMapper;
	
	@Autowired
	ArgUserProgressMapper userProgressMapper;
	
}
