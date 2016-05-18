package com.yubo.wechat.content.service;

import org.springframework.stereotype.Service;

import com.yubo.wechat.content.service.textspeaker.TextContentPool;
import com.yubo.wechat.content.vo.ReplyInput;

/**
 * 回复处理
 * 
 * @author young.jason
 *
 */
@Service
public class ReplyService {

	/**
	 * 
	 * @param replyInput
	 * @return
	 */
	public String replyText(ReplyInput replyInput) {

		return TextContentPool.getRandomText();
	}
}
