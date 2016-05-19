package com.yubo.wechat.api.service.impl;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.EventMsgRequest;
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.content.service.ReplyService;
import com.yubo.wechat.support.redis.RedisHandler;
import com.yubo.wechat.support.redis.RedisKeyBuilder;
import com.yubo.wechat.user.service.UserPetFavorService;
import com.yubo.wechat.user.service.UserService;
import com.yubo.wechat.user.vo.UserVO;

/**
 * 摸Mo业务处理
 * @author young.jason
 *
 */
@Service
public class MoMoService implements MessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(MoMoService.class);
	
	@Autowired
	ReplyService replyService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserPetFavorService userPetFavorService;
	
	@Autowired
	RedisHandler redisHandler;
	
	@Value("${simpletalk.redis.cache.duration:900}")
	private int simpleTalkCacheDuration;
	
	@Value("${favor.lock.duration:1800}")
	private int favorLockDuration;
	
	public MsgHandlerResult execute(MsgInputParam param) {

		logger.info("摸Mo业务处理");
		
		try {
			EventMsgRequest request = XMLHelper.parseXml(param.requestBody, EventMsgRequest.class);
			
			//获取回复语句
			// 根据目前的时间段，获取一个回复
			String replyContent = buildContent(param);
			
			// 若回复是一个非命令回复，将回复存储到Redis中，并定时15分钟的有效时间
			insertReplyToCache(param,replyContent);
			
			return buildResult(request,replyContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 若回复是一个命令回复，则将回复存储到MySQL talk_history中，永久等待用户下次回复（暂定永久）
		return null;
	}
	
	/**
	 * 若回复是一个非命令回复，将回复存储到Redis中，并定时15分钟的有效时间<br/>
	 * key构建规则：simpleTalk.${petId}.${userId}，value为宠物的话
	 * 
	 * @param param
	 * @param replyContent
	 */
	private void insertReplyToCache(MsgInputParam param, String replyContent) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
			String key = RedisKeyBuilder.buildSimpleTalkKey(param.userId,1);
			redis.set(key, replyContent);
			redis.expire(key, simpleTalkCacheDuration);
		} catch (Exception e) {
			return;
		}
	}


	/**
	 * 构建结果
	 * @param request
	 * @param content 
	 * @return
	 * @throws JAXBException
	 */
	private MsgHandlerResult buildResult(EventMsgRequest request, String content) throws JAXBException {

		TextResponse response = new TextResponse();
		response.setContent(content);
		response.setCreateTime(System.currentTimeMillis());
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());
		
		MsgHandlerResult result = new MsgHandlerResult();
		result.setXmlResponse(XMLHelper.buildXMLStr(response, TextResponse.class));
		return result;
	}

	/**
	 * 构建内容
	 * @param param 
	 * @return
	 */
	private String buildContent(MsgInputParam param) {

		StringBuffer content = new StringBuffer("");
		
		//主要回复内容
		String mainText = replyService.smartReply(null);
		content.append(mainText);
		
//		UserVO userVO = userService.getUserVOByUserId(param.userId);
		
		//去缓存中检查，如果没有亲密度的标记了，则说明可以增加亲密度了
		if(!favorLock(param)){
			int favorPoint = userPetFavorService.getFavorPoint(param.userId,param.petId);
			if(favorPoint>0){
				//加入半个小时的锁，不允许半小时内再增加亲密度
				addFavorLock(param);
				userPetFavorService.addFavor(param.userId,param.petId,favorPoint);
				content.append("\n【YUBO亲密度+").append(favorPoint).append("】");
			}
		}
		return content.toString();
	}

	private void addFavorLock(MsgInputParam param) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
			String key = RedisKeyBuilder.buildFavorLockKey(param.userId, param.petId);
			redis.set(key, "1");
			redis.expire(key, favorLockDuration);
		} catch (Exception e) {
			logger.error("Redis操作addFavorLock失败");
		}
	}

	private boolean favorLock(MsgInputParam param) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
			String lock = redis.get(RedisKeyBuilder.buildFavorLockKey(param.userId, param.petId));
			return lock!=null;
		} catch (Exception e) {
			logger.error("Redis操作favorLock失败，无法加亲密度");
			return true;
		}
	}
	
	
	

}
