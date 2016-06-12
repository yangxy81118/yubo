package com.yubo.feeder.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yubo.feeder.SessionConstraint;

public class LoginFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Object loginState = req.getSession().getAttribute(SessionConstraint.ATTR_LOGIN_STATE);
		if(loginState!=null){
				chain.doFilter(req, resp);
		}else{
			resp.sendRedirect("/index");
		}
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
