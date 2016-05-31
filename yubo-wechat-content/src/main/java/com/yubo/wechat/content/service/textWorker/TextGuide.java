package com.yubo.wechat.content.service.textWorker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.yubo.wechat.content.dao.MessageTextMapper;
import com.yubo.wechat.content.dao.pojo.MessageText;
import com.yubo.wechat.content.vo.MessageVO;
import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.support.MathUtil;
import com.yubo.wechat.support.PageUtil;
import com.yubo.wechat.support.TimeUtil;
import com.yubo.wechat.support.thread.ThreadPool;

/**
 * 文本回复内容引导者<br/>
 * 之所以称之为“引导”，是因为后台存储了不同时段触发的若干内容池<br/>
 * 而根据不同的时段，TextGuide会进行相应的内容流入切换，只切换到当前可用的内容池上，其余的闲置
 * 
 * @author yangxy8
 *
 */
@Component
public class TextGuide {

	private static final String DEFAULT_SAYS = "稍等，YUBO去个洗手间，一会就回来= =|||";

	/**
	 * 所有TextPool
	 */
	private List<TextPool> allTextPool = new ArrayList<>();

	/**
	 * 当前正在生效的TextPool
	 */
	private List<TextPool> activeTextPool = new ArrayList<>();

	/**
	 * 在本工程启动的时候，TextGuide启动加载流程<br/>
	 * 1. 从数据库中获取要加载的TextPool队列，获取各自的ID与起止时间 2. 多线程异步加载每个TextPool<br/>
	 * 3. 加载完毕之后，TextGuide根据当前时间，定位到需要的TextPool，加入到activeTextPool
	 * 
	 */
	@PostConstruct
	public void init() {
		prepareAllTextPool();
		buildTextPoolContent();
		prepareRadomRate();
	}

	/**
	 * 随机获取当前有效的内容
	 * 
	 * @return
	 */
	public MessageVO getRandomText() {

		double random = Math.random();
		for (int i = 0; i < activeTextPool.size(); i++) {
			TextPool textPool = activeTextPool.get(i);
			if (textPool.getAccessRdmEnd() > random
					&& textPool.getAccessRdmStart() < random) {
				return getRandomMessageInPool(textPool);
			}
		}

		MessageVO defaultVO = new MessageVO();
		defaultVO.setContent(DEFAULT_SAYS);
		return defaultVO;
	}

	private MessageVO getRandomMessageInPool(TextPool textPool) {
		List<MessageVO> list = textPool.getTextContent();
		int idx = (int) (Math.random() * list.size());
		return list.get(idx);
	}

	/**
	 * 对当前的activePool进行随机数概率的平分
	 */
	private void prepareRadomRate() {
		if (activeTextPool.size() == 0) {
			return;
		}

		double totalMsgCount = 0;
		for (TextPool textPool : activeTextPool) {
			totalMsgCount += textPool.getTextContent().size();
		}

		if (totalMsgCount == 0) {
			return;
		}

		double lastEnd = 0;
		for (TextPool textPool : activeTextPool) {
			double rate = textPool.getTextContent().size() / totalMsgCount;
			BigDecimal bd = new BigDecimal(rate);
			rate = bd.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
			textPool.setAccessRdmStart(lastEnd);
			textPool.setAccessRdmEnd(rate + lastEnd);
			lastEnd = rate + lastEnd;
		}
		
		logger.info("加载YUBO语言内容完毕，共{}条数据",(int)totalMsgCount);
	}

	/**
	 * 构建每个TextPool中的内容
	 * 
	 */
	private void buildTextPoolContent() {

		List<Future<Integer>> futureList = new ArrayList<>();
		ExecutorService threaPool = ThreadPool.FIXED_POOL;
		for (TextPool textPool : allTextPool) {
			futureList.add(threaPool.submit(new TextPoolLoadRunner(textPool,
					this, messageTextMapper)));
		}

		for (Future<Integer> future : futureList) {
			try {
				future.get(5, TimeUnit.SECONDS);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		// 构建period_id为0（全天）的内容
		buildAllTimeText();
	}

	private void buildAllTimeText() {
		TextPool allTimePool = new TextPool();
		allTimePool.setPeriodId(0);

		// 加载数据
		Map<String, Object> param = new HashMap<>();
		param.put("periodId", 0);
		int totalRows = messageTextMapper.countByParam(param);

		if (totalRows != 0) {
			int pageCount = PageUtil.pageCount(totalRows, PAGE_ROWS);
			List<MessageVO> contentlist = new ArrayList<>();

			for (int i = 0; i < pageCount; i++) {
				Map<String, Object> contentParam = new HashMap<>();
				contentParam.put("periodId", 0);
				contentParam.put("startRow", i * PAGE_ROWS);
				contentParam.put("rowCount", PAGE_ROWS);
				contentlist.addAll(buildReplyMessageList(messageTextMapper
						.pageByParam(contentParam)));
			}

			allTimePool.setTextContent(contentlist);
		}

		allTextPool.add(allTimePool);
		activeTextPool.add(allTimePool);

		// 每天0点生效
		Calendar cal = TimeUtil.getChinaCalendar(1, 0, 0, 0);
		Timer timer = new Timer();
		timer.schedule(new AllTimePoolTimer(allTimePool, this), cal.getTime());
		logger.info("TextPool[0]在明天0点执行更新任务");

	}

	// TODO 这个方法需要进行复用优化
	private List<MessageVO> buildReplyMessageList(List<MessageText> paramList) {
		List<MessageVO> result = new ArrayList<>();
		if (EmptyChecker.isEmpty(paramList)) {
			return result;
		}

		for (MessageText message : paramList) {
			MessageVO vo = new MessageVO();
			vo.setContent(message.getContent());
			vo.setFunctionCode(message.getFuncCode());
			result.add(vo);
		}

		return result;
	}

	private void prepareAllTextPool() {
		Properties props = null;
		try {
			props = PropertiesLoaderUtils
					.loadAllProperties("app-schedule.properties");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		Set<Entry<Object, Object>> propElements = props.entrySet();
		for (Entry<Object, Object> entry : propElements) {
			String key = entry.getKey().toString();

			if (key.startsWith(KEY_SCHEDULE_PREFIX)) {
				TextPool textPool = new TextPool();
				String values[] = entry.getValue().toString().split(",");
				textPool.setStartHour(Integer.parseInt(values[0]));
				textPool.setEndHour(Integer.parseInt(values[1]));
				String[] ktemp = key.split("\\.");
				textPool.setPeriodId(Integer.parseInt(ktemp[ktemp.length - 1]));
				allTextPool.add(textPool);
			}
		}
	}

	/**
	 * 将配置中的小时换算成一个具体的时间
	 * 
	 * @param string
	 * @return
	 */
	private static long parseTime(String hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTimeInMillis();
	}

	private List<Integer> getAllPeriodIds() {

		Map<String, Object> param = null;
		;
		List<Map<String, Integer>> result = messageTextMapper
				.countByGroup(param);
		if (EmptyChecker.isEmpty(result)) {
			return null;
		}

		List<Integer> allPeriodIds = new ArrayList<>();
		for (Map<String, Integer> map : result) {
			allPeriodIds.add(Integer.parseInt(map.get("periodId").toString()));
		}

		return allPeriodIds;
	}

	/**
	 * 加入
	 * 
	 * @param pool
	 */
	public synchronized void addActivePool(TextPool pool) {
		activeTextPool.add(pool);
	}

	/**
	 * 移除某个特定的线程池
	 * 
	 * @param poolToRemove
	 */
	public synchronized void removeActivePool(TextPool poolToRemove) {
		Iterator<TextPool> it = activeTextPool.iterator();
		while (it.hasNext()) {
			TextPool nextItem = it.next();
			if (nextItem.getPeriodId().equals(poolToRemove.getPeriodId())) {
				it.remove();
			}
		}
	}

	public synchronized List<TextPool> getAllTextPool() {
		return allTextPool;
	}

	public synchronized List<TextPool> getActiveTextPool() {
		return activeTextPool;
	}

	@Autowired
	MessageTextMapper messageTextMapper;

	public MessageTextMapper getMessageTextMapper() {
		return messageTextMapper;
	}

	private static final String KEY_SCHEDULE_PREFIX = "textworker.schedule.";

	private static final Logger logger = LoggerFactory
			.getLogger(TextGuide.class);

	private static final int PAGE_ROWS = 30;
}
