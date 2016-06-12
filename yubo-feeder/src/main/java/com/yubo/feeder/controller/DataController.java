package com.yubo.feeder.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户个人信息相关请求入口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/pet")
public class DataController {

	@RequestMapping("/home")
	public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("feeder-home.html");
		return view;
	}
	
	@RequestMapping("/json/datagrid")
	public ModelAndView datagrid(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		
		JSONObject root = new JSONObject();
		
		int total = 15;
		root.put("total", total);

		JSONArray rows = new JSONArray();
		
		
		
		for (int i = 0; i < total; i++) {
			JSONObject row = new JSONObject();
			row.put("svgId", i);
			row.put("svgTag", "商店"+i);
			row.put("svgContent", getContent());
			rows.add(row);
		}
		
		root.put("rows", rows);
		
		String j = root.toJSONString();
		writer.write(j);
		return null;
	}
	
	private Object getContent() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(new File("/home/yangxy8/svg.txt")));
		
		String readLine = "";
		StringBuffer sb = new StringBuffer();
		while((readLine=reader.readLine())!=null){
			sb.append(readLine);
		}
		
		reader.close();

		return sb.toString();
	}


	@RequestMapping("/json/product")
	public ModelAndView product(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		PrintWriter writer = response.getWriter();
		String j = "[{\"productid\":\"FI-SW-01\",\"productname\":\"Koi\"},{\"productid\":\"K9-DL-01\",\"productname\":\"Dalmation\"},{\"productid\":\"RP-SN-01\",\"productname\":\"Rattlesnake\"},{\"productid\":\"RP-LI-02\",\"productname\":\"Iguana\"},{\"productid\":\"FL-DSH-01\",\"productname\":\"Manx\"},{\"productid\":\"FL-DLH-02\",\"productname\":\"Persian\"},{\"productid\":\"AV-CB-01\",\"productname\":\"Amazon Parrot\"}]";
		writer.write(j);
		return null;
	}
}