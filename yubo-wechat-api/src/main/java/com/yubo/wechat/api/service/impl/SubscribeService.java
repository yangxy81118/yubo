package com.yubo.wechat.api.service.impl;

import java.util.Date;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgContextParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.EventMsgRequest;
import com.yubo.wechat.api.xml.request.WeChatRequest;
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.content.service.ReplyService;
import com.yubo.wechat.content.vo.MessageVO;
import com.yubo.wechat.pet.service.PetService;
import com.yubo.wechat.support.PetID;
import com.yubo.wechat.support.redis.RedisHandler;
import com.yubo.wechat.support.redis.RedisKeyBuilder;
import com.yubo.wechat.user.service.UserPetFavorService;
import com.yubo.wechat.user.service.UserService;
import com.yubo.wechat.user.service.UserTalkingService;
import com.yubo.wechat.user.vo.UserTalkVO;
import com.yubo.wechat.user.vo.UserVO;

/**
 * 摸Mo业务处理
 * 
 * @author young.jason
 *
 */
@Service
public class SubscribeService implements MessageHandler {

	public MsgHandlerResult execute(MsgContextParam param) throws Exception {

		logger.info("摸Mo业务处理");

		// 如果是宠物还未出生的阶段，则直接回复
		if (petService.stillInEgg(1)) {
			return buildResult(param.request,
					"神秘旁白:\n小宠物还在孵化之中，还需要更多同学来激活，完成小宠物的孵化。\n快去告诉身边的同学吧~");
		}

		// 获取回复语句
		// 根据目前的时间段，获取一个回复
		String replyContent = buildContent(param);

		return XMLHelper.buildTextResponse(replyContent, param.request);
	}

	/**
	 * 若回复是一个非命令回复，将回复存储到Redis中，并定时15分钟的有效时间<br/>
	 * key构建规则：simpleTalk.${petId}.${userId}，value为宠物的话
	 * 
	 * @param param
	 * @param replyContent
	 * @param functionCode
	 */
	private void insertReplyToCache(MsgContextParam param, String replyContent,
			Integer functionCode) {
		functionCode = functionCode == null ? 0 : functionCode;
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
			String key = RedisKeyBuilder.buildTalkKey(param.userId, 1,
					functionCode);
			redis.set(key, replyContent);
			redis.expire(key, simpleTalkCacheDuration);
		} catch (Exception e) {
			return;
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
	private MsgHandlerResult buildResult(WeChatRequest request, String content)
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

	/**
	 * 构建内容
	 * 
	 * @param param
	 * @return
	 */
	private String buildContent(MsgContextParam param) {

		StringBuffer content = new StringBuffer("");

		// 主要回复内容
		MessageVO replyMessage = replyService.smartReply(null);
		String mainText = replyMessage.getContent();
		content.append(mainText);

		// 去缓存中检查，如果没有亲密度的标记了，则说明可以增加亲密度了
		if (!favorLock(param)) {
			int favorPoint = userPetFavorService.getFavorPoint(param.userId,
					param.petId);
			if (favorPoint > 0) {
				// 加入半个小时的锁，不允许半小时内再增加亲密度
				addFavorLock(param);
				userPetFavorService.addFavor(param.userId, param.petId,
						favorPoint);
				content.append("\n【YUBO亲密度+").append(favorPoint).append("】");
			}
		}

		// 如果replyMessage中有functionCode，将其记录到redis中，等待用户下次回复
		if (null != replyMessage.getFunctionCode()) {
			try {
				Jedis redisClient = redisHandler.getRedisClient();
				redisClient.set(RedisKeyBuilder.buildFunctionCode(param.userId,
						param.petId), replyMessage.getFunctionCode() + "");
			} catch (Exception e) {
				logger.error("存储用户功能性回复的FunctionCode失败:" + e.getMessage(), e);
			}
		} else {
			try {
				Jedis redisClient = redisHandler.getRedisClient();
				redisClient.del(RedisKeyBuilder.buildFunctionCode(param.userId,
						param.petId));
			} catch (Exception e) {
				logger.error("清除用户功能性回复的FunctionCode失败:" + e.getMessage(), e);
			}
			// 首先清除掉之前的functioncode记录
			// 任何回复，只要都加入到缓存中半个小时，半小时之内回复，即存储下来
			insertReplyToCache(param, mainText, replyMessage.getFunctionCode());
		}
		return content.toString();
	}

	private void addFavorLock(MsgContextParam param) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
			String key = RedisKeyBuilder.buildFavorLockKey(param.userId,
					param.petId);
			redis.set(key, "1");
			redis.expire(key, favorLockDuration);
		} catch (Exception e) {
			logger.error("Redis操作addFavorLock失败", e);
		}
	}

	private boolean favorLock(MsgContextParam param) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
			String lock = redis.get(RedisKeyBuilder.buildFavorLockKey(
					param.userId, param.petId));
			return lock != null;
		} catch (Exception e) {
			logger.error("Redis操作favorLock失败，无法加亲密度", e);
			return true;
		}
	}

	private static final Logger logger = LoggerFactory
			.getLogger(SubscribeService.class);

	@Autowired
	ReplyService replyService;

	@Autowired
	UserService userService;

	@Autowired
	UserTalkingService userTalkingService;

	@Autowired
	UserPetFavorService userPetFavorService;

	@Autowired
	RedisHandler redisHandler;

	@Value("${simpletalk.redis.cache.duration:900}")
	private int simpleTalkCacheDuration;

	@Value("${favor.lock.duration:1800}")
	private int favorLockDuration;

	@Autowired
	PetService petService;

}
