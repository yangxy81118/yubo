package com.yubo.wechat.api.xml.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 图文消息
 * 
 * @author young.jason
 *
 */

@XmlRootElement(name = "xml")
public class ViewResponse extends MsgResponse {

	/*
	 * <?xml version="1.0" encoding="utf-8"?>
	 * 
	 * <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[fromUser]]></FromUserName>
	 * <CreateTime>12345678</CreateTime> <MsgType><![CDATA[news]]></MsgType>
	 * <ArticleCount>2</ArticleCount> <Articles> <item>
	 * <Title><![CDATA[title1]]></Title>
	 * <Description><![CDATA[description1]]></Description>
	 * <PicUrl><![CDATA[picurl]]></PicUrl> <Url><![CDATA[url]]></Url> </item>
	 * <item> <Title><![CDATA[title]]></Title>
	 * <Description><![CDATA[description]]></Description>
	 * <PicUrl><![CDATA[picurl]]></PicUrl> <Url><![CDATA[url]]></Url> </item>
	 * </Articles> </xml>
	 */

	@XmlElement(name = "ArticleCount")
	private int articleCount;

	@XmlElement(name = "Articles")
	private Article article = new Article();

	@XmlElement(name = "MsgType")
	public String getMsgType() {
		return wrapWithCDATA("news");
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}
	
	public void setItems(List<ArticleItem> items) {
		article.setItems(items);
	}
	
	static class Article{
		
		@XmlElement(name = "item")
		private List<ArticleItem> items;
		
		public void setItems(List<ArticleItem> items) {
			this.items = items;
		}
	}
	

}
