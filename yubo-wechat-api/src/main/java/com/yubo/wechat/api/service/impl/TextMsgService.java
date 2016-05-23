package com.yubo.wechat.api.service.impl;

import java.util.Date;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.helper.AuthorizeHelper;
import com.yubo.wechat.api.service.helper.VoteHelper;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.TextMsgRequest;
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.content.service.ReplyService;
import com.yubo.wechat.pet.service.PetService;
import com.yubo.wechat.support.redis.RedisHandler;
import com.yubo.wechat.support.redis.RedisKeyBuilder;
import com.yubo.wechat.user.service.UserService;
import com.yubo.wechat.user.vo.SimpleTalkVO;
import com.yubo.wechat.vote.service.VoteService;

/**
 * 文本消息处理
 * 
 * @author young.jason
 *
 */
@Service
public class TextMsgService implements MessageHandler {

	public MsgHandlerResult execute(MsgInputParam param) {

		logger.info("TextMsg业务处理");
		
		try {
			TextMsgRequest request = XMLHelper.parseXml(param.requestBody,
					TextMsgRequest.class);

			// 激活验证流程第一
			if (isAuthorizing(request)) {
				return authorizeHelper.execute(param, request);
			}
			
			// 如果是宠物还未出生的阶段，则直接回复
			if (petService.stillInEgg(1)) {
				return buildResult(request,
						"神秘旁白:\n小宠物还在孵化之中，还需要更多同学来激活，完成小宠物的孵化。\n快去告诉身边的同学吧~");
			}
			
			// 临时测试用
			MsgHandlerResult testR = null;
			if ((testR = voteHelper.testForQuestion(request)) != null) {
				return testR;
			}

			// 投票检查
			if (voteHelper.isVoteAnswer(request.getContent())) {
				return voteHelper.execute(param, request);
			}
			
			//进入最常规流程
			
			String petLastTalk = null;

			// 首先判断去Redis中进行查找，key为simpleTalk.${petId}.${userId}
			// TODO 这里有加入事务的必要性
			if ((petLastTalk = petLastTalkInCache(param)) != null) {
				saveSimpleTalk(petLastTalk, param, request.getContent());
				removeRedisKey(param.userId, 1);
				logger.info("成功存储用户[{}]的回复[{}]", param.userId,
						request.getContent());
				return buildResult(request, replyService.shortReply());
			}

			// 如果不存在，则检查是否是命令回复，去检查数据库talk_history表
			// 若在talk_history中找到了，则说明是命令回复，将本次回复进行记录，进行对应的业务处理（学笑话，学外语等等）

			return buildResult(request, replyService.shortReply());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	// TODO 经常这样删除,不知是否这样合适？
	private void removeRedisKey(int userId, int petId) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
			redis.del(RedisKeyBuilder.buildSimpleTalkKey(userId, petId));
		} catch (Exception e) {
			logger.error("Redis操作removeRedisKey失败");
		}
	}

	private void saveSimpleTalk(String petSaid, MsgInputParam param,
			String userSaid) {
		SimpleTalkVO simpleTalkVO = new SimpleTalkVO();
		simpleTalkVO.setPetId(1);
		simpleTalkVO.setUserId(param.userId);
		simpleTalkVO.setPetSaid(petSaid);
		simpleTalkVO.setUserSaid(userSaid);
		simpleTalkVO.setTalkFuncCode(0);
		simpleTalkVO.setLastTalkTime(new Date());
		userService.saveSimpleTalk(simpleTalkVO);
	}

	/**
	 * 是否在缓存中存在聊天记录
	 * 
	 * @param param
	 * 
	 * @return
	 */
	private String petLastTalkInCache(MsgInputParam param) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String key = RedisKeyBuilder.buildSimpleTalkKey(param.userId, 1);
		String content = redis.get(key);
		return content;
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

	/**
	 * 检查是否是激活操作
	 * 
	 * @param request
	 * 
	 * @return
	 */
	private boolean isAuthorizing(TextMsgRequest request) {
		String content = request.getContent();
		return content.startsWith("#JH");
	}

	private static final Logger logger = LoggerFactory
			.getLogger(TextMsgService.class);

	@Autowired
	RedisHandler redisHandler;

	@Autowired
	UserService userService;

	@Autowired
	ReplyService replyService;

	@Autowired
	VoteService voteService;

	@Autowired
	VoteHelper voteHelper;

	@Autowired
	AuthorizeHelper authorizeHelper;

	@Autowired
	PetService petService;
}
