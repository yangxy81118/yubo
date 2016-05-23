package com.yubo.wechat.vote.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.vote.service.VoteCache;

/**
 * 投票内容刷新任务
 * 
 * @author young.jason
 *
 */
@Service
public class VoteReloadJob {
	
	@Autowired
	VoteCache voteCache;
	
	public void reloadVote(){
		System.out.println("执行了");
	}
}
