package com.yubo.wechat.api.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.xml.request.TextMsgRequest;
import com.yubo.wechat.api.xml.request.WeChatRequest;
import com.yubo.wechat.api.xml.response.ArticleItem;
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.api.xml.response.ViewResponse;
import com.yubo.wechat.vote.service.vo.AnswerEntry;

/**
 * XML对对象Bean的转换
 * 
 * @author young.jason
 *
 */
public class XMLHelper {

	/**
	 * 对执行的Bean对象转换为xml
	 * 
	 * @param bean
	 *            要转换的对象
	 * @param clz
	 *            对应的class
	 * @return
	 * @throws JAXBException
	 */
	public static String buildXMLStr(Object bean, Class<?> clz)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clz);
		Marshaller marshaller = context.createMarshaller();

		StringWriter writer = new StringWriter();
		marshaller.marshal(bean, writer);

		// TODO 这个有性能隐患，之后要想办法解决
		String xmlResult = writer.toString().replace("&lt;", "<")
				.replace("&gt;", ">").replace("&amp;", "&");
		return xmlResult;
	}

	public static <T> T parseXml(String xml, Class<T> clz)
			throws JAXBException, XMLStreamException {

		long s = System.currentTimeMillis();
		JAXBContext context = JAXBContext.newInstance(clz);
		Unmarshaller un = context.createUnmarshaller();

		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		StringReader reader = new StringReader(xml);
		XMLStreamReader xmler = xmlif.createXMLStreamReader(reader);

		Object obj = un.unmarshal(xmler);
		T unpackObj = (T) obj;
		// System.out.println("parse:"+(System.currentTimeMillis()-s));
		return unpackObj;
	}

	public static void main(String[] args) throws JAXBException,
			XMLStreamException {

		long s = System.currentTimeMillis();
		WeChatRequest obj = parseXml(
				"<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[fromUser]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[thisisatest]]></Content><MsgId>1234567890123456</MsgId></xml>",
				WeChatRequest.class);
		System.out.println(System.currentTimeMillis() - s);

		// System.out.println(obj);

	}

	public static MsgHandlerResult buildTextResponse(String content,
			WeChatRequest request) throws JAXBException {
		TextResponse response = new TextResponse();
		response.setContent(content);
		response.setCreateTime(System.currentTimeMillis());
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());
		MsgHandlerResult result = new MsgHandlerResult();
		result.setXmlResponse(XMLHelper.buildXMLStr(response,
				TextResponse.class));

		return result;
	}

	public static MsgHandlerResult buildSingleViewResponse(
			WeChatRequest request, String description,
			String title, String url,String picUrl) throws JAXBException {

		ViewResponse response = new ViewResponse();
		response.setCreateTime(System.currentTimeMillis());
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());
		response.setArticleCount(1);

		List<ArticleItem> articles = new ArrayList<>();
		ArticleItem item = new ArticleItem();
		item.setDescription(description);
		item.setTitle(title);
		item.setUrl(url);
		item.setPicUrl(picUrl);
		articles.add(item);
		response.setItems(articles);

		MsgHandlerResult result = new MsgHandlerResult();
		result.setXmlResponse(XMLHelper.buildXMLStr(response,
				ViewResponse.class));
		return result;
	}
}
