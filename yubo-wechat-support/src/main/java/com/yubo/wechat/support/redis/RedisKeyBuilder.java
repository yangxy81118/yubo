package com.yubo.wechat.support.redis;

/**
 * Redis key构建工具类
 * 
 * @author young.jason
 *
 */
public class RedisKeyBuilder {

	public static final String PREFIX_SIMPLETALK = "SimpleTalk.";
	public static final String PREFIX_FAVOR_LOCK = "FavorLock.";

	/**
	 * 简单对话Redis Key 构建
	 * 
	 * @param userId
	 * @param petId
	 * @return
	 */
	public static String buildSimpleTalkKey(int userId, int petId) {
		return buildByUserAndPet(PREFIX_SIMPLETALK, userId, petId);
	}
	
	/**
	 * 亲密度定时锁
	 * 
	 * @param userId
	 * @param petId
	 * @return
	 */
	public static String buildFavorLockKey(int userId, int petId) {
		return buildByUserAndPet(PREFIX_FAVOR_LOCK, userId, petId);
	}

	public static String buildByUserAndPet(String prefix, int userId, int petId) {
		StringBuffer keyBuf = new StringBuffer(prefix);
		keyBuf.append(petId).append(".").append(userId);
		String key = keyBuf.toString();
		return key;
	}
}
