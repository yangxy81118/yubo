package com.yubo.wechat.api.service.helper;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.TextMsgRequest;
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.support.MathUtil;
import com.yubo.wechat.support.redis.RedisHandler;
import com.yubo.wechat.support.redis.RedisKeyBuilder;
import com.yubo.wechat.user.service.FeederService;

/**
 * 管理员登录验证业务处理
 * 
 * @author yangxy8
 *
 */
@Component
public class FeederLoginHelper {

	/**
	 * 检查该用户是否是授权的管理员
	 * 
	 * @param param
	 * @return
	 */
	public boolean checkFeeder(MsgInputParam param) {
		return feederService.getFeederInfoByWechatId(param.wechatId) != null;
	}

	/**
	 * 创建用户登录密码
	 * 
	 * @param param
	 * @return
	 * 
	 */
	public MsgHandlerResult createLoginPwd(MsgInputParam param,
			TextMsgRequest request) {

		String password = MathUtil.getRandomString(8);
		String key = RedisKeyBuilder.buildKey(
				RedisKeyBuilder.PREFIX_FEEDER_LOGIN, password);
		try {
			Jedis redis = redisHandler.getRedisClient();
			redis.set(key, param.wechatId);
			redis.expire(key, 60 * 15);
			return buildResult(request, "请在15分钟内用密码" + password
					+ "登录小宠物饲养系统~");
		} catch (Exception e) {
			logger.error("创建管理员登录密码失败" + e.getMessage(), e);
			try {
				return buildResult(request, "宠物系统出现一些问题，稍等片刻哦~ *o*");
			} catch (JAXBException e1) {
				return null;
			}
		}

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

	private static final Logger logger = LoggerFactory
			.getLogger(FeederLoginHelper.class);

	@Autowired
	FeederService feederService;

	@Autowired
	RedisHandler redisHandler;

}
