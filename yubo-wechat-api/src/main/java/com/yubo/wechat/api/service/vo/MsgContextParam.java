package com.yubo.wechat.api.service.vo;

import com.yubo.wechat.api.xml.request.WeChatRequest;

/**
 * 各种事件业务处理共用参数打包类
 * 
 * @author young.jason
 *
 */
public class MsgContextParam {

	public WeChatRequest request;
	public int userId;
	public String wechatId;
	public int petId = 1;
}
