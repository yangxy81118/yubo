package com.yubo.wechat.content.vo;

import java.util.List;

/**
 * 回复VO
 * 
 * @author yangxy8
 *
 */
public class MessageVO {

	/**
	 * 回复文字内容
	 */
	private String content;

	/**
	 * 功能编码，这种类型的回复，往往要等待用户的下一句回复的内容
	 */
	private Integer functionCode;

	/**
	 * 分享用户功能性输入的前缀候选文字
	 */
	private List<String> sharePrefixList;

	public List<String> getSharePrefixList() {
		return sharePrefixList;
	}

	public void setSharePrefixList(List<String> sharePrefixList) {
		this.sharePrefixList = sharePrefixList;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(Integer functionCode) {
		this.functionCode = functionCode;
	}

}
