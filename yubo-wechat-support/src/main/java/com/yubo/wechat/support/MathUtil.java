package com.yubo.wechat.support;

public class MathUtil {

	/**
	 * 获取分钟
	 * @param mills
	 * @return
	 */
	public static int getMinute(long mills){
		return (int)(mills/MILLS_PET_MINUTE);
	}
	
	private static final int MILLS_PET_MINUTE = 60 * 1000;
	
}
