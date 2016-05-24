package com.yubo.wechat.api.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yubo.wechat.vote.service.VoteRealTimeHandler;

/**
 * 用户个人信息相关请求入口
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/pet")
public class PetController {

	@Autowired
	VoteRealTimeHandler handler;


	@RequestMapping("/level")
	public ModelAndView shopIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer =response.getWriter();
		writer.write("Hello,小宠物等级:1<br/>");
		writer.write("当前投票结果：");
		writer.write(handler.getVoteResult().toString());
		return null;
	}
}
