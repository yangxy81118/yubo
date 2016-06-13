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
@RequestMapping("/svg")
public class SvgController {

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("svg-manager.html");
	}
	
	@RequestMapping("/load")
	@ResponseBody
	public Object load(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer rows) throws Exception {
		List<SvgVO> svgList = svgService.paging(page, rows);
		return svgList;
	}
	
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Object add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
//		List<SvgVO> svgList = svgService.(page, rows);
//		return svgList;
		
		PrintWriter writer = response.getWriter();
		
		String tag = request.getParameter("svgTag");
		String content = request.getParameter("svgContent");
		
		SvgVO vo = new SvgVO();
		vo.setSvgTag(tag);
		vo.setSvgContent(content);
		
		try{
			svgService.add(vo);
		}catch(Exception e){
			writer.write("500");
		}
		writer.write("200");
		return null;
	}
	
	
	@Autowired
	SvgService svgService;

}