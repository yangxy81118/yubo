package com.yubo.wechat.support.redis;

/**
 * Redis key构建工具类
 * 
 * @author young.jason
 *
 */
public class RedisKeyBuilder {

	public static final String PREFIX_SIMPLETALK = "SimpleTalk.";

	/**
	 * 简单对话Redis Key 构建
	 * @param userId
	 * @param petId
	 * @return
	 */
	public static String buildSimpleTalkKey(int userId, int petId) {
		StringBuffer keyBuf = new StringBuffer(PREFIX_SIMPLETALK);
		keyBuf.append(petId).append(".").append(userId);
		String key = keyBuf.toString();
		return key;
	}

}
