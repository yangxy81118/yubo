package com.yubo.feeder.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yubo.feeder.service.SvgService;
import com.yubo.feeder.vo.SvgVO;

/**
 * 用户个人信息相关请求入口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/vote")
public class VoteController {

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("vote-manage.html");
	}

}