package com.yubo.wechat.api.service.vo;

/**
 * 消息出列结果<br/>
 * 两个属性为互斥关系
 * 
 * @author young.jason
 *
 */
public class MsgHandlerResult {

	/**
	 * XML结果
	 */
	private String xmlResponse;
	
	/**
	 * 返回的H5页面地址
	 */
	private String viewPath;

	public String getXmlResponse() {
		return xmlResponse;
	}

	public void setXmlResponse(String xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public String getViewPath() {
		return viewPath;
	}

	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
	}
	
	
	
	
}
