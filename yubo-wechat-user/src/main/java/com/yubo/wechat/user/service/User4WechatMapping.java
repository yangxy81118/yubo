package com.yubo.wechat.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yubo.wechat.support.PageUtil;
import com.yubo.wechat.user.dao.UserBaseMapper;
import com.yubo.wechat.user.dao.pojo.UserBase;

/**
 * 用户ID与微信OpenID的映射
 * @author young.jason
 *
 */
@Component
public class User4WechatMapping {

	private Map<String,Integer> wechat4UserId = new HashMap<>();
	
	@Autowired
	UserBaseMapper userBaseMapper;
	
	private static final int PAGE_SIZE = 30;
	
	@PostConstruct
	public void loadMapping(){
		int totalCount = getCount();
		PageUtil.pageCount(totalCount, PAGE_SIZE);
			
	}

	private int getCount() {
		return userBaseMapper.countByParam(new UserBase());
	}
}
