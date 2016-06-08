package com.yubo.feeder.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;

import com.yubo.wechat.support.redis.RedisHandler;
import com.yubo.wechat.support.redis.RedisKeyBuilder;

/**
 * 用户个人信息相关请求入口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("")
public class LoginController {

	@RequestMapping(value="/index")
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return new ModelAndView("index.html");
	}
	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String password = request.getParameter("pwd");
		Jedis redis = redisHandler.getRedisClient();
		String wechatId = redis.get(RedisKeyBuilder.buildKey(RedisKeyBuilder.PREFIX_FEEDER_LOGIN,password));
		
		
		return null;
	}
	
	@Autowired
	RedisHandler redisHandler;
}
