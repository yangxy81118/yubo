package com.yubo.wechat.support;
/**
 * 分页工具
 * 
 * @author young.jason
 *
 */
public class PageUtil {

	/**
	 * 计算页数
	 * 
	 * @param count
	 * @param pageSize
	 * @return
	 */
	public static int pageCount(int count, int pageSize) {
		if (count <= 0 || pageSize <= 0)
			return 0;
		return count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
	}

}
