package com.yubo.wechat.api.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.UserWeChatIdHelper;

/**
 * Controller公共父类
 * @author yangxy8
 *
 */
public class BaseController {

	/**
	 * 获取微信XML报文
	 * @param request
	 * @return
	 * @throws IOException
	 */
	protected String buildRequestBody(HttpServletRequest request)
			throws IOException {

		String requestBody = "";
		request.setCharacterEncoding("UTF-8");
		BufferedReader br = request.getReader();
		String inputLine;
		try {
			while ((inputLine = br.readLine()) != null) {
				requestBody += inputLine;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return requestBody;
	}
	
	/**
	 * 获取微信ID
	 * @param requestBody
	 * @return
	 * @throws JAXBException
	 */
	protected String getWeChatID(String requestBody) throws JAXBException {
		UserWeChatIdHelper helper = XMLHelper.parseXml(requestBody,
				UserWeChatIdHelper.class);
		return helper.getFromUserName();
	}

}
