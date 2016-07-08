package com.yubo.feeder.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yubo.feeder.AjaxResponseCode;
import com.yubo.feeder.SessionConstraint;

/**
 * 用户登录过滤器
 * @author yangxy8
 *
 */
public class LoginFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Object loginState = req.getSession().getAttribute(
				SessionConstraint.ATTR_LOGIN_STATE);
		
		System.out.println(request.getParameter("ajax"));
		
		if (loginState != null) {
			chain.doFilter(req, resp);
		} else {
			Object ajaxIdentified = request.getParameter(SessionConstraint.AJAX_IDENTIFIED);
			if(ajaxIdentified!=null){
				PrintWriter writer = response.getWriter();
				JSONObject rspsObj = new JSONObject();
				rspsObj.put("code", AjaxResponseCode.SESSION_EXPIRED);
				rspsObj.put("msg", "登录操作已过期，请进行重新登录");
				writer.write(rspsObj.toJSONString());
			}else{
				resp.sendRedirect("/index");
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

	private static final Logger logger = LoggerFactory
			.getLogger(LoginFilter.class);

}
