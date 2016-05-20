package com.yubo.wechat.api.xml.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author young.jason
 *
 */
public class ArticleItem extends BaseXML {


	private String title;
	

	private String description;
	

	private String picUrl;
	

	private String url;

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement(name="Title")
	public String getTitle() {
		return wrapWithCDATA(title);
	}
	
	@XmlElement(name="Description")
	public String getDescription() {
		return wrapWithCDATA(description);
	}

	@XmlElement(name="PicUrl")
	public String getPicUrl() {
		return wrapWithCDATA(picUrl);
	}

	@XmlElement(name="Url")
	public String getUrl() {
		return wrapWithCDATA(url);
	}
	
	
	
	
	
}
