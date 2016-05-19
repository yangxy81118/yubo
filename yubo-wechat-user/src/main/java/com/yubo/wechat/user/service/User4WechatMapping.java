package com.yubo.wechat.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yubo.wechat.support.PageUtil;
import com.yubo.wechat.user.dao.UserBaseMapper;
import com.yubo.wechat.user.dao.pojo.UserBase;

/**
 * 用户ID与微信OpenID的映射
 * 
 * @author young.jason
 *
 */
@Component
public class User4WechatMapping {

	
	private Map<String, Integer> wechat4UserId = new HashMap<>();

	@PostConstruct
	public void loadMapping() {
		
		logger.info("开始加载微信ID与用户ID的映射");
		int totalCount = getCount();
		int pageCount = PageUtil.pageCount(totalCount, PAGE_SIZE);
		for (int i = 0; i < pageCount; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("startRow", i * PAGE_SIZE);
			param.put("rowCount", PAGE_SIZE);
			List<UserBase> list = userBaseMapper.selectByParam(param);
			for (UserBase userBase : list) {
				wechat4UserId.put(userBase.getWechatId(), userBase.getUserId());
			}
		}
		logger.info("开始加载微信ID与用户ID的映射加载完毕，一共{}条数据",wechat4UserId.size());

	}

	private int getCount() {
		return userBaseMapper.countByParam(new UserBase());
	}
	
	/**
	 * 运行时新增的用户加入到map中<br/>
	 * TODO 保证一致性，这里是否有风险？
	 * 
	 * @param wechatId
	 * @param userId
	 */
	public synchronized void addNewUser(String wechatId,int userId){
		wechat4UserId.put(wechatId,userId);
	}
	
	/**
	 * 根据微信ID获取用户ID
	 * @param wechatId
	 * @return
	 */
	public synchronized Integer getUserId(String wechatId){
		return wechat4UserId.get(wechatId);
	}
	
	private static final int PAGE_SIZE = 30;
	
	@Autowired
	UserBaseMapper userBaseMapper;
	
	private static final Logger logger = LoggerFactory
			.getLogger(UserService.class);

}
