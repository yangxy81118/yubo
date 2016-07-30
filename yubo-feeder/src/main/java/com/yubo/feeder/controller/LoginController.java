package com.yubo.feeder.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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

	@RequestMapping(value = "/slf4j")
	public ModelAndView slf4j(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		org.apache.log4j.LogManager.resetConfiguration();
		PropertyConfigurator con = new PropertyConfigurator();
		con.doConfigure(
				"/home/yangxy8/server/tomcat-server-2/webapps/yubo-feeder/WEB-INF/classes/log4j.properties",
				org.apache.log4j.LogManager.getLoggerRepository());

		return null;
	}

	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("index.html");
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("LOGIN");
		String password = request.getParameter("pwd");
		Jedis redis = redisHandler.getRedisClient();
		String wechatId = redis.get(RedisKeyBuilder.buildKey(
				RedisKeyBuilder.PREFIX_FEEDER_LOGIN, password));
		logger.debug("Login User WeChatId:{}", wechatId);
		if (validWeChatId(wechatId)) {
			PrintWriter writer = response.getWriter();
			writer.print("200");
			setLoginToken(request);
			return null;
		} else {
			PrintWriter writer = response.getWriter();
			writer.print("Error!");
			return null;
		}
	}

	private void setLoginToken(HttpServletRequest request) {
		request.getSession().setAttribute("loginState", true);
	}

	private boolean validWeChatId(String wechatId) {
		return !StringUtils.isEmpty(wechatId);
	}

	@Autowired
	RedisHandler redisHandler;

	private static final Logger logger = LogManager
			.getLogger(LoginController.class.getName());
}
