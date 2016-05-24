package com.yubo.wechat.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 投票展示信息入口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/vote")
public class VoteViewController {

	/**
	 * 获取某一次投票的信息
	 * 
	 * @param request
	 * @param response
	 * @param voteId
	 *            要查询的投票ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/detail")
	public ModelAndView voteDetail(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int voteId)
			throws Exception {

		// request先获取用户ID

		// 通过voteId与userId获取用户的投票情况

		// 组建数据:
		/*
		 * 投票问题 投票备选项 投票备选项各自选择人数 投票起止日期 我自己投票的那个选项（高亮，突出，或者啥的）
		 */

		return null;
	}
	
	
	/**
	 * 获取最近一定次数的投票信息列表
	 * 
	 * @param request
	 * @param response
	 * @param voteId
	 *            要查询的投票ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ModelAndView voteList(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int recentRows)
			throws Exception {
		
		// 直接去获取最近recentRows条投票记录信息
		/*
		 * 投票ID，问题，日期
		 */

		return null;
	}
}
