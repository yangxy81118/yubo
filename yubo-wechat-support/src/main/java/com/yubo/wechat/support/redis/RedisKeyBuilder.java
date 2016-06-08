package com.yubo.wechat.support.redis;

/**
 * Redis key构建工具类
 * 
 * @author young.jason
 *
 */
public class RedisKeyBuilder {

	public static final String PREFIX_SIMPLETALK = "Talk.";
	public static final String PREFIX_FAVOR_LOCK = "FavorLock.";
	public static final String PREFIX_FUNCTION_CODE = "FunctionCode.";
	public static final String PREFIX_FEEDER_LOGIN = "FeederLogin.";

	/**
	 * 对话Redis Key 构建
	 * 
	 * @param userId
	 * @param petId
	 * @return
	 */
	public static String buildKey(String... args) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
		}
		return sb.toString();
	}
	
	/**
	 * 对话Redis Key 构建
	 * 
	 * @param userId
	 * @param petId
	 * @return
	 */
	public static String buildTalkKey(int userId, int petId,int functionCode) {
		return buildByUserAndPet(PREFIX_SIMPLETALK, userId, petId,functionCode);
	}
	
	
	/**
	 * 当前用户最近一次功能性对话的code
	 * 
	 * @param userId
	 * @param petId
	 * @return
	 */
	public static String buildFunctionCode(int userId, int petId) {
		return buildByUserAndPet(PREFIX_FUNCTION_CODE,userId, petId);
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
	
	public static String buildByUserAndPet(String prefix, int userId, int petId,int functionCode) {
		StringBuffer keyBuf = new StringBuffer(prefix);
		keyBuf.append(petId).append(".").append(userId).append(".").append(functionCode);
		String key = keyBuf.toString();
		return key;
	}
}
