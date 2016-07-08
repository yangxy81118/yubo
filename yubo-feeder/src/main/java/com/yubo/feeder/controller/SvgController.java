package com.yubo.feeder.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.yubo.feeder.vo.Query;
import com.yubo.feeder.vo.SvgSelectVO;
import com.yubo.feeder.vo.SvgVO;
import com.yubo.wechat.support.MathUtil;

/**
 * 用户个人信息相关请求入口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/home/svg")
public class SvgController {

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("svg-manage.html");
	}
	
	/**
	 * SVG查询
	 * @param word 查询关键字
	 * @param sort 排序字段
	 * @param order 排序规则
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/load")
	@ResponseBody
	public DatagridResponse<SvgVO> load(HttpServletRequest request,
			HttpServletResponse response,Query<SvgVO> query) throws Exception {
		DatagridResponse<SvgVO> svgList = svgService.paging(query);
		
		//计算svg大小
		List<SvgVO> list = svgList.getRows();
		for (int i = 0; i < list.size(); i++) {
			SvgVO vo = list.get(i);
			vo.setSvgLength(MathUtil.getRint(vo.getSvgContent().length()/1024.0,2));
		}
		return svgList;
	}
	
	
	
	/**
	 * 用来选择svg
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/load/select")
	@ResponseBody
	public DatagridResponse<SvgSelectVO> loadSelect(HttpServletRequest request,
			HttpServletResponse response,Query<SvgVO> query) throws Exception {
		//强制固定50个
		query.setRows(50);
		DatagridResponse<SvgVO> svgList = svgService.paging(query);
		DatagridResponse<SvgSelectVO> svgSelectList = formatSvgSelect(svgList);
		return svgSelectList;
	}
	
	/**
	 * 要改成每行10个的格式
	 * @param svgList
	 * @return
	 */
	private DatagridResponse<SvgSelectVO> formatSvgSelect(
			DatagridResponse<SvgVO> svgList) {

		List<SvgVO> svgSourceRows = svgList.getRows();
		List<SvgSelectVO> svgSelectRows = new ArrayList<>();
		
		SvgSelectVO selectVO = new SvgSelectVO();
		for (int i = 1; i <= svgSourceRows.size(); i++) {
			int offset = i%10;
			SvgVO vo = svgSourceRows.get(i-1);
			if(offset!=0){
				vo.setSvgIconIndex(i-1);
				putSvgToSelect(selectVO,offset,vo);
			}else{
				vo.setSvgIconIndex(i-1);
				selectVO.setSvgTen(vo);
				svgSelectRows.add(selectVO);
				selectVO = new SvgSelectVO();
			}
		}
		svgSelectRows.add(selectVO);
		
		DatagridResponse<SvgSelectVO> result = new DatagridResponse<>();
		result.setTotal(svgList.getTotal());
		result.setRows(svgSelectRows);
		return result;
	}

	private void putSvgToSelect(SvgSelectVO selectVO, int offset, SvgVO svgVO) {
		switch (offset) {
		case 1:
			selectVO.setSvgOne(svgVO);
			break;
		case 2:
			selectVO.setSvgTwo(svgVO);
			break;
		case 3:
			selectVO.setSvgThree(svgVO);
			break;
		case 4:
			selectVO.setSvgFour(svgVO);
			break;
		case 5:
			selectVO.setSvgFive(svgVO);
			break;
		case 6:
			selectVO.setSvgSix(svgVO);
			break;
		case 7:
			selectVO.setSvgSeven(svgVO);
			break;
		case 8:
			selectVO.setSvgEight(svgVO);
			break;
		case 9:
			selectVO.setSvgNine(svgVO);
			break;
		default:
			break;
		}
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