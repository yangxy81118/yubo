package com.yubo.feeder.controller;

import java.io.PrintWriter;
import java.util.Iterator;
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
import com.yubo.feeder.vo.DatagridResponse;
import com.yubo.feeder.vo.SvgVO;
import com.yubo.wechat.support.MathUtil;

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
		return new ModelAndView("svg-manage.html");
	}
	
	@RequestMapping("/load")
	@ResponseBody
	public DatagridResponse<SvgVO> load(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer rows) throws Exception {
		DatagridResponse<SvgVO> svgList = svgService.paging(page, rows);
		
		//计算svg大小
		List<SvgVO> list = svgList.getRows();
		for (int i = 0; i < list.size(); i++) {
			SvgVO vo = list.get(i);
			vo.setSvgLength(MathUtil.getRint(vo.getSvgContent().length()/1024.0,2));
		}
		return svgList;
	}
	
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Object add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
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
	
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public Object update(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		PrintWriter writer = response.getWriter();
		
		String id = request.getParameter("svgId");
		String tag = request.getParameter("svgTag");
		String content = request.getParameter("svgContent");
		
		SvgVO vo = new SvgVO();
		vo.setSvgId(Integer.parseInt(id));
		vo.setSvgTag(tag);
		vo.setSvgContent(content);
		
		try{
			svgService.update(vo);
		}catch(Exception e){
			writer.write("500");
		}
		writer.write("200");
		return null;
	}
	
	
	@Autowired
	SvgService svgService;

}