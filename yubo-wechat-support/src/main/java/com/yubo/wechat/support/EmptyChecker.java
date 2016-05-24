package com.yubo.wechat.support;

import java.util.Collection;

public class EmptyChecker {

	/**
	 * 判断一个结合是否为空
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection){
		return collection == null || collection.size() <= 0;
	}
}
