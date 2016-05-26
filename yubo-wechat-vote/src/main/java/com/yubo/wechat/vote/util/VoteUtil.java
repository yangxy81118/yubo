package com.yubo.wechat.vote.util;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.yubo.wechat.vote.service.vo.AnswerResultEntry;

/**
 * 投票小工具类
 * 
 * @author young.jason
 *
 */
public class VoteUtil {

	/**
	 * 获取胜利方
	 * @param voteResult
	 * @return
	 */
	public static AnswerResultEntry getWinner(String answerSummeryJSON) {
		AnswerResultEntry winner = new AnswerResultEntry();
		JSONObject json = JSONObject.parseObject(answerSummeryJSON);
		Set<Entry<String, Object>> answers = json.entrySet();

		int maxCount = 0;
		double totalCount = 0;
		for (Entry<String, Object> entry : answers) {
			int count = Integer.parseInt(entry.getValue().toString());
			totalCount += count;
			if (maxCount < count) {
				winner.setKey(entry.getKey());
				winner.setCount(count);
			}
		}

		winner.setRate((int) (Math.rint(winner.getCount() / totalCount)));

		return winner;
	}

	/**
	 * 获取胜利方
	 * @param voteResult
	 * @return
	 */
	public static AnswerResultEntry getWinner(Map<String, Integer> voteResult) {

		AnswerResultEntry winner = new AnswerResultEntry();
		Set<Entry<String, Integer>> answers = voteResult.entrySet();

		int maxCount = 0;
		double totalCount = 0;
		for (Entry<String, Integer> entry : answers) {
			int count = entry.getValue();
			totalCount += count;
			if (maxCount < count) {
				winner.setKey(entry.getKey());
				winner.setCount(count);
			}
		}

		winner.setRate((int) (Math.rint(winner.getCount() / totalCount)));

		return winner;

	}
}
