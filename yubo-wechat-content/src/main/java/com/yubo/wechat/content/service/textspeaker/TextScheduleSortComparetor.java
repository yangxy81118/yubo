package com.yubo.wechat.content.service.textspeaker;

import java.util.Comparator;

/**
 * 文本准备定时任务时间顺序排序工具
 * 
 * @author young.jason
 *
 */
public class TextScheduleSortComparetor implements Comparator<TextScdlEntry> {

	/**
	 * 按照entry.index从小到大排列
	 */
	public int compare(TextScdlEntry entry1, TextScdlEntry entry2) {
		return entry1.periodId - entry2.periodId;
	}

}
