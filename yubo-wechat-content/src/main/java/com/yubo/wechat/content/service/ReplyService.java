package com.yubo.wechat.content.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yubo.wechat.content.service.textWorker.TextGuide;
import com.yubo.wechat.content.service.textlearning.TextTeacher;
import com.yubo.wechat.content.vo.MessageVO;
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

	@Value("${functiontext.random.chance:0.05}")
	private Double functionRdmChance;
	
	/**
	 * 随机获取当前文本内容
	 * 
	 * @param replyInput
	 * @return
	 */
	public MessageVO smartReply(ReplyInput replyInput) {
		double random = Math.random();
		if (random < functionRdmChance) {
			return textTeacher.getRandomText();
		}else{
			return textGuide.getRandomText();
		}
		
	}
	
	/**
	 * 最简单的回话，一般不会用来去引发提问性的对话
	 * @return
	 */
	public String shortReply(){
		int index = (int)(Math.random()*shortSentences.length);
		return shortSentences[index];
	}
	
	
	@Autowired
	TextGuide textGuide;
	
	@Autowired
	TextTeacher textTeacher;
}
