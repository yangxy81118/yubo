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

	static String[] shortSentences = new String[]{"sorry,YUBO还听不懂，不过我马上就会学会的～有空就摸摸我吧～"};
	
	
	/**
	 * 
	 * @param replyInput
	 * @return
	 */
	public String smartReply(ReplyInput replyInput) {

		return TextContentPool.getRandomText();
	}
	
	/**
	 * 最简单的回话，一般不会用来去引发提问性的对话
	 * @return
	 */
	public String shortReply(){
		int index = (int)(Math.random()*shortSentences.length);
		return shortSentences[index];
	}
}
