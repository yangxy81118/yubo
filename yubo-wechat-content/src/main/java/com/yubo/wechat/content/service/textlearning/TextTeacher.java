package com.yubo.wechat.content.service.textlearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yubo.wechat.content.dao.MessageTextMapper;
import com.yubo.wechat.content.service.textWorker.TextPool;
import com.yubo.wechat.content.vo.FunctionTalkVO;
import com.yubo.wechat.content.vo.MessageVO;
import com.yubo.wechat.support.thread.ThreadPool;

/**
 * 文本学习工具类
 * @author yangxy8
 *
 */
@Component
@PropertySource("classpath:app-core.properties")
public class TextTeacher {

	private Map<Integer,FunctionTalkVO> funcTalkConfig = new HashMap<>();
	
	/**
	 * 当前正在生效的功能性回复TextPool,由于其内容会随时间出现不可控的扩大，所以其概率必须限定咋可控范围内
	 */
	private List<TextPool> functionTextPool = new ArrayList<>();
	
	/**
	 * 加载用户的功能性回复
	 * @param textGuide
	 */
	@PostConstruct
	public void loadText() {
		
		//首先从message_text中分析出有多少种func_code
		List<Integer> funcCodeList = getFuncCodeList();
		
		//对每个func_code在talk_history中进行多线程的读取,每个MessageVO的内容：文本，funcCode，前缀文本
		buildFunctionShareText(funcCodeList);

	}
	
	public MessageVO getRandomText() {

		if(functionTextPool.size() > 0){
			TextPool pool = functionTextPool.get(0);
			return getRandomMessageInPool(pool);
		}else{
			MessageVO defaultVO = new MessageVO();
			defaultVO.setContent(DEFAULT_SAYS);
			return defaultVO;
		}
	}
	
	private MessageVO getRandomMessageInPool(TextPool textPool) {
		List<MessageVO> list = textPool.getTextContent();
		Random random = new Random();
		int idx = random.nextInt(list.size());
		MessageVO findVO = list.get(idx);
		
		MessageVO returnVO = new MessageVO();
		returnVO.setFunctionCode(findVO.getFunctionCode());
		returnVO.setContent(buildFuncShare(findVO.getSharePrefixList(),findVO.getContent()));
		return returnVO;
	}
	
	private String buildFuncShare(List<String> sharePrefixList, String content) {
		Random random = new Random();
		int idx = random.nextInt(sharePrefixList.size());
		String prefix = sharePrefixList.get(idx);
		return prefix + " "+content;
	}

	private void buildFunctionShareText(List<Integer> funcCodeList) {
		
		List<MessageVO> allFunctionList = new ArrayList<>();
		List<Future<List<MessageVO>>> futureList = new ArrayList<>();
		ExecutorService threaPool = ThreadPool.FIXED_POOL;
		for (int i=0 ; i < funcCodeList.size(); i++) {
			Integer code = funcCodeList.get(i);
			futureList.add(threaPool.submit(new FunctionTextLoadRunner(funcCodeList.get(i),messageTextMapper,funcTalkConfig.get(code))));
		}

		for (Future<List<MessageVO>> future : futureList) {
			try {
				allFunctionList.addAll(future.get());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		futureList = null;
		
		TextPool functionPool = new TextPool();
		functionPool.setTextContent(allFunctionList);
		functionTextPool.add(functionPool);
		
		logger.info("加载FunctionTalk数据完毕，共{}条数据",allFunctionList.size());
	}

	private List<Integer> getFuncCodeList() {

		Map<String, Object> param = new HashMap<>();
		param.put("funcCode", 1);
		List<Map<String, Object>> result = messageTextMapper.countByGroup(param);
		
		List<Integer> funcCode = new ArrayList<>();
		for (Map<String, Object> row : result) {
			Object codeObj = row.get("funcCode");
			if(codeObj==null){
				continue;
			}
			
			Integer fc = Integer.parseInt(codeObj.toString());
			if(fc.equals(0)){
				continue;
			}
			funcCode.add(fc);
			
			FunctionTalkVO ftvo = new FunctionTalkVO();
			ftvo.setFunctionCode(fc);
			parseFunctionConfig(ftvo,row);
			funcTalkConfig.put(fc,ftvo);
		}
		
		return funcCode;
	}

	private void parseFunctionConfig(FunctionTalkVO ftvo,
			Map<String, Object> row) {
		String config = row.get("funcConfig").toString();
		JSONObject obj = JSONObject.parseObject(config);
		JSONArray replyArray = obj.getJSONArray("reply");
		
		List<String> reply = new ArrayList<>();
		for (int i = 0; i < replyArray.size(); i++) {
			reply.add(replyArray.getString(i));
		}
		
		JSONArray shareArray = obj.getJSONArray("share_prefix");
		List<String> share = new ArrayList<>();
		for (int i = 0; i < shareArray.size(); i++) {
			share.add(shareArray.getString(i));
		}
		
		ftvo.setPetReply(reply);
		ftvo.setSharePrefix(share);
	}

	@Autowired
	MessageTextMapper messageTextMapper;
	
	private static final Logger logger = LoggerFactory
			.getLogger(TextTeacher.class);

	private static final String DEFAULT_SAYS = "稍等，YUBO去个洗手间，一会就回来= =|||";
	
}
