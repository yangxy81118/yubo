package com.yubo.wechat.api.xml.response;


public class BaseXML {

	/**
	 * 对数据内容进行CDATA标签包裹
	 * 
	 * @param source
	 * @return
	 */
	protected String wrapWithCDATA(String source) {
		StringBuffer sb = new StringBuffer("<![CDATA[");
		sb.append(source).append("]]>");
		return sb.toString();
	}

}
