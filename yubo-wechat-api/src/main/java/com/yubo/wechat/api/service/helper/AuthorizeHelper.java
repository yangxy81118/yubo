package com.yubo.wechat.api.service.helper;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.TextMsgRequest;
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.pet.service.PetService;
import com.yubo.wechat.user.service.UserAuthorizeService;
import com.yubo.wechat.user.vo.AuthorizeVO;

/**
 * 激活验证流程处理类
 * 
 * @author young.jason
 *
 */
@Component
@PropertySource("classpath:app-core.properties")
public class AuthorizeHelper {

	@Value("${pet.born.step.point:0.01}")
	private double bornPoint;
	
	
	/**
	 * 核心处理过程
	 * @param param
	 * @param request
	 * @return
	 * @throws JAXBException 
	 */
	public MsgHandlerResult execute(MsgInputParam param, TextMsgRequest request) throws JAXBException{
		
		if(userAuthorizeService.userIsAuthorized(param.userId)){
			return buildResult(request,"你已经激活过了，不需要再进行激活了~");
		}
		
		String code = request.getContent().replace("#JH", "").trim();
		
		//对验证码进行检查
		AuthorizeVO vo = userAuthorizeService.authorizeInfoByCode(code,param.userId);
		
		//如果无法对应到任何记录，即不正确，提示错误
		if(vo==null){
			return buildResult(request,"对不起您的验证码好像不正确，请再仔细核对一下验证码哦");
		}
		
		//如果正确，对宠物的成长进行加入成长指数
		//TODO 这个需要进行配置
		petService.growUp(1,bornPoint);
		
		//给出相关的信息
		String sayHello = buildHello(vo);
		
		return buildResult(request,sayHello);
	}
	
	
	private String buildHello(AuthorizeVO vo) {

		StringBuffer hello = new StringBuffer("神秘旁白:\n");
		hello.append(vo.getStudentClass()+"的"+vo.getStudentName()+"同学你好~\n");
		hello.append("你的激活已经成功，");
		
		if(petService.stillInEgg(1)){
			hello.append("所以咱们的小宠物离出生又近了一步了，想必TA已迫不及待要出来看看这个世界了！\n把这个好消息告诉身边的同学吧~");
		}else{
			hello.append("欢迎随时来找我玩~虽然我又笨又懒~但是说不定在你们的帮助下我就变聪明变勤奋哦~");
		}
		
		return hello.toString();
	}


	/**
	 * 构建结果
	 * 
	 * @param request
	 * @param content
	 * @return
	 * @throws JAXBException
	 */
	private MsgHandlerResult buildResult(TextMsgRequest request, String content)
			throws JAXBException {

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
	
	@Autowired
	UserAuthorizeService userAuthorizeService;
	
	@Autowired
	PetService petService;
}

