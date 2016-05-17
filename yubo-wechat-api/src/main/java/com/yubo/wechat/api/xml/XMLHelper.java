package com.yubo.wechat.api.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.yubo.wechat.api.xml.request.TextMsgRequest;
import com.yubo.wechat.api.xml.response.TextResponse;

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
		String xmlResult = writer.toString().replace("&lt;![", "<![")
				.replace("]]&gt;", "]]>");
		return xmlResult;
	}

	public static <T> T parseXml(String xml,Class<T> clz) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(clz);
		Unmarshaller un = context.createUnmarshaller();
		StringReader reader = new StringReader(xml);
		Object obj = un.unmarshal(reader);
		T unpackObj = (T)obj;
		return unpackObj;
	}

	public static void main(String[] args) throws JAXBException {

//		TextResponse rsp = new TextResponse();
//		rsp.setContent("123");
//		rsp.setCreateTime(41234324l);
//		rsp.setFromUserName("abc");
//		rsp.setToUserName("bbbccc");
//
//		System.out.println(buildXMLStr(rsp,TextResponse.class));
		
		TextMsgRequest obj = parseXml("<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[fromUser]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[thisisatest]]></Content><MsgId>1234567890123456</MsgId></xml>",
				TextMsgRequest.class);
		System.out.println(obj);

	}

}
