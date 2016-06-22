package com.yubo.wechat.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yubo.wechat.vote.service.VoteRealTimeHandler;

/**
 * 用户个人信息相关请求入口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/")
public class ManualController {

	@Autowired
	VoteRealTimeHandler handler;

	@RequestMapping("/manual")
	public ModelAndView shopIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("manual.html");
		return view;
	}
}
