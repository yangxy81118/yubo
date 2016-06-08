package com.yubo.wechat.support;

import java.util.Random;

public class MathUtil {

	/**
	 * 获取分钟
	 * 
	 * @param mills
	 * @return
	 */
	public static int getMinute(long mills) {
		return (int) (mills / MILLS_PET_MINUTE);
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length 字符串长度
	 * @return
	 */
	public static String getRandomString(int length) { 
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	

	private static final int MILLS_PET_MINUTE = 60 * 1000;

}
