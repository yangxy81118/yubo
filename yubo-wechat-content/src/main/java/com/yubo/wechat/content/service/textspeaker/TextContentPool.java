package com.yubo.wechat.content.service.textspeaker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * 回复的文字内容容器
 * @author young.jason
 *
 */
public class TextContentPool {

	private static List<String> currentTextList = new ArrayList<>();
	
	/**
	 * 随机获取一个当前时间段内容池中的内容
	 * @return
	 */
	public synchronized static String getRandomText(){
		
		int length = currentTextList.size();
		int randomIndx = (int)(Math.random()*length);
		String content = currentTextList.get(randomIndx);
		if(!StringUtils.isEmpty(content)){
			return content;
		}else{
			return DEFAULT_SAYS;
		}
	}
	
	/**
	 * 更新内容池
	 * 
	 * @param newList
	 */
	public synchronized static void setCurrentTextList(List<String> newList){
		currentTextList = newList;
	}
	
	private static final String DEFAULT_SAYS = "YUBO有点不舒服，等会~ *o* ";
}
