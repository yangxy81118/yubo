package com.yubo.wechat.content.service.textlearning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.web.client.RestClientException;

import com.yubo.wechat.content.dao.MessageTextMapper;
import com.yubo.wechat.content.vo.FunctionTalkVO;
import com.yubo.wechat.content.vo.MessageVO;
import com.yubo.wechat.support.PageUtil;

/**
 * 功能性回复内容异步读取工具
 * 
 * @author yangxy8
 *
 */
public class FunctionTextLoadRunner implements Callable<List<MessageVO>> {

	private Integer functionCode;
	private MessageTextMapper messageTextMapper;
	private FunctionTalkVO functionTalkVO;

	public FunctionTextLoadRunner(Integer functionCode,
			MessageTextMapper messageTextMapper, FunctionTalkVO functionTalkVO) {
		this.functionCode = functionCode;
		this.messageTextMapper = messageTextMapper;
		this.functionTalkVO = functionTalkVO;
	}

	@Override
	public List<MessageVO> call() throws Exception {

		List<MessageVO> contentlist = new ArrayList<>();
		int totalRows = messageTextMapper.countUserFuncTalk(functionCode);

		if (totalRows == 0) {
			return contentlist;
		}

		int pageCount = PageUtil.pageCount(totalRows, PAGE_ROWS);

		for (int i = 0; i < pageCount; i++) {
			Map<String, Object> contentParam = new HashMap<>();
			contentParam.put("functionCode", functionCode);
			contentParam.put("startRow", i * PAGE_ROWS);
			contentParam.put("rowCount", PAGE_ROWS);
			contentlist.addAll(buildReplyMessageList(messageTextMapper
					.selectUserFuncTalk(contentParam)));
		}

		return contentlist;
	}

	private List<MessageVO> buildReplyMessageList(List<String> talkResult) {
		
		List<MessageVO> result = new ArrayList<>();
		for (int i = 0; i < talkResult.size(); i++) {
			String replyContent  = talkResult.get(i);
			MessageVO message = new MessageVO();
			message.setContent(replyContent);
			message.setFunctionCode(functionCode);
			message.setSharePrefixList(functionTalkVO.getSharePrefix());
			result.add(message);
		}
		return result;
	}

	private static final int PAGE_ROWS = 30;

}
