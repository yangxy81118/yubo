package com.yubo.wechat.api.service.impl;

import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.helper.AuthorizeHelper;
import com.yubo.wechat.api.service.helper.FeederLoginHelper;
import com.yubo.wechat.api.service.helper.FunctionTalkHelper;
import com.yubo.wechat.api.service.helper.VoteHelper;
import com.yubo.wechat.api.service.vo.MsgContextParam;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.WeChatRequest;
import com.yubo.wechat.api.xml.response.TextResponse;
import com.yubo.wechat.content.service.ReplyService;
import com.yubo.wechat.content.service.textlearning.TextTeacher;
import com.yubo.wechat.content.vo.FunctionTalkVO;
import com.yubo.wechat.pet.service.PetService;
import com.yubo.wechat.support.redis.RedisHandler;
import com.yubo.wechat.support.redis.RedisKeyBuilder;
import com.yubo.wechat.user.service.UserService;
import com.yubo.wechat.user.service.UserTalkingService;
import com.yubo.wechat.user.vo.UserTalkVO;
import com.yubo.wechat.vote.service.VoteService;

/**
 * 文本消息处理
 * 
 * @author young.jason
 *
 */
@Service
public class TextMsgService implements MessageHandler {

	public MsgHandlerResult execute(MsgContextParam param) {

		param.petId = 1;

		try {

			WeChatRequest request = param.request;

			// 激活验证
			if (isAuthorizing(request)) {
				return authorizeHelper.execute(param, request);
			}

			// 登录密码获取
			if (isFeederLogin(request) && feederLoginHelper.checkFeeder(param)) {
				return feederLoginHelper.createLoginPwd(param, request);
			}

			// 如果是宠物还未出生的阶段，则直接回复
			if (petService.stillInEgg(1)) {
				return XMLHelper.buildTextResponse(
						"神秘旁白:\n小宠物还在孵化之中，还需要更多同学来激活，完成小宠物的孵化。\n快去告诉身边的同学吧~",
						request);
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

			// 进入最常规流程

			String petLastTalk = null;

			// 首先若在talk_history中找到了，则说明是命令回复，将本次回复进行记录，进行对应的业务处理（学笑话，学外语等等）
			Jedis redisClient = redisHandler.getRedisClient();
			String funcCodeStr = redisClient.get(RedisKeyBuilder
					.buildFunctionCode(param.userId, param.petId));
			if (funcCodeStr != null) {
				if (userTalkingService.userRejection(request.getContent()
						.trim())) {
					redisClient.del(RedisKeyBuilder.buildFunctionCode(
							param.userId, param.petId));
					return XMLHelper.buildTextResponse("好的，想到记得下次告诉我哦～",request);
				} else {
					UserTalkVO functionTalkVO = new UserTalkVO();
					Integer fCode = Integer.parseInt(funcCodeStr);
					functionTalkVO.setTalkFuncCode(fCode);
					functionTalkVO.setUserSaid(request.getContent());
					functionTalkVO.setUserId(param.userId);
					functionTalkVO.setPetId(param.petId);
					userTalkingService.addUserTalk(functionTalkVO);
					redisClient.del(RedisKeyBuilder.buildFunctionCode(
							param.userId, param.petId));

					// TODO 这里该如何回复?暂时固定
					return XMLHelper.buildTextResponse(getFTalkReply(textTeacher.getFTVOByCode(fCode)),request);
				}
			}

			// 判断去Redis中进行查找，key为simpleTalk.${petId}.${userId}.0，查找简单
			// TODO 这里有加入事务的必要性
			if ((petLastTalk = petLastTalkInCache(param)) != null) {
				saveSimpleTalk(petLastTalk, param, request.getContent());
				removeRedisKey(param.userId, param.petId, 0);
				logger.info("成功存储用户[{}]的回复[{}]", param.userId,
						request.getContent());
				return XMLHelper.buildTextResponse(replyService.shortReply(),request);
			}

			return XMLHelper.buildTextResponse(replyService.shortReply(),request);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	private boolean isFeederLogin(WeChatRequest request) {
		String content = request.getContent();
		return content.startsWith("#denglu");
	}

	/**
	 * 随机获取功能性回答
	 * 
	 * @param ftvoByCode
	 * @return
	 */
	private String getFTalkReply(FunctionTalkVO talkVO) {
		List<String> reply = talkVO.getPetReply();
		int idx = (int) (Math.random() * reply.size());
		return reply.get(idx);
	}

	// TODO 经常这样删除,不知是否这样合适？
	private void removeRedisKey(int userId, int petId, int i) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
			redis.del(RedisKeyBuilder.buildTalkKey(userId, petId, 0));
		} catch (Exception e) {
			logger.error("Redis操作removeRedisKey失败");
		}
	}

	private void saveSimpleTalk(String petSaid, MsgContextParam param,
			String userSaid) {
		UserTalkVO simpleTalkVO = new UserTalkVO();
		simpleTalkVO.setPetId(1);
		simpleTalkVO.setUserId(param.userId);
		simpleTalkVO.setPetSaid(petSaid);
		simpleTalkVO.setUserSaid(userSaid);
		simpleTalkVO.setTalkFuncCode(0);
		simpleTalkVO.setLastTalkTime(new Date());
		userTalkingService.saveSimpleTalk(simpleTalkVO);
	}

	/**
	 * 是否在缓存中存在聊天记录
	 * 
	 * @param param
	 * 
	 * @return
	 */
	private String petLastTalkInCache(MsgContextParam param) {
		Jedis redis = null;
		try {
			redis = redisHandler.getRedisClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String key = RedisKeyBuilder.buildTalkKey(param.userId, 1, 0);
		String content = redis.get(key);
		return content;
	}

	/**
	 * 检查是否是激活操作
	 * 
	 * @param request
	 * 
	 * @return
	 */
	private boolean isAuthorizing(WeChatRequest request) {
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
	UserTalkingService userTalkingService;

	@Autowired
	ReplyService replyService;

	@Autowired
	VoteService voteService;

	@Autowired
	VoteHelper voteHelper;

	@Autowired
	FeederLoginHelper feederLoginHelper;

	@Autowired
	TextTeacher textTeacher;

	@Autowired
	FunctionTalkHelper functionTalkHelper;

	@Autowired
	AuthorizeHelper authorizeHelper;

	@Autowired
	PetService petService;
}
