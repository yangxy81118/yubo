package com.yubo.wechat.api;

/**
 * 微信消息推送事件枚举
 * @author young.jason
 *
 */
public enum WeChatEventType {

	/**
	 * 文本消息
	 */
	TEXT,
	/**
	 * 摸Mo按钮
	 */
	CLICK_MOMO,
	/**
	 * 我的投票
	 */
	CLICK_MY_VOTE,
	
	/**
	 * 多媒体事件按钮
	 */
	MEDIA,
	
	/**
	 * 未知
	 */
	UNKNOWN;
}
