package com.yubo.wechat.content;

import java.util.Collection;

/**
 * 
 * Description: TODO 小工具<br/>
 * 
 * @author: huangGuotao
 * @date: 2015年4月14日 下午6:14:41
 * @version: 1.0
 * @since: JDK 1.7
 */
public class CatsAndDogs {
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

    /**
     * 检查一个集合是否为空
     * 
     * @author yangxiaoyu
     * @date: 2016年4月19日 上午10:33:30
     * @version 1.0
     *
     * @param collection
     * @return
     */
    public static boolean notEmpty(Collection collection) {
        return collection != null && collection.size() > 0;
    }
}
