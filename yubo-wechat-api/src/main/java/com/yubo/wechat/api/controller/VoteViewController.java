package com.yubo.wechat.api.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.yubo.wechat.api.controller.model.VoteChoiceItem;
import com.yubo.wechat.vote.service.VoteAnswerCache;
import com.yubo.wechat.vote.service.VoteService;
import com.yubo.wechat.vote.service.vo.AnswerResultEntry;
import com.yubo.wechat.vote.service.vo.UserVoteVO;
import com.yubo.wechat.vote.service.vo.VoteVO;

/**
 * 投票展示信息入口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/vote")
public class VoteViewController {

	private static final String DEFAULT_LOOK = null;

	/**
	 * 获取某一次投票的信息
	 * 
	 * @param request
	 * @param response
	 * @param voteId
	 *            要查询的投票ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/detail")
	public ModelAndView voteDetail(HttpServletRequest request,
			HttpServletResponse response, @RequestParam long voteId,
			@RequestParam int userId) throws Exception {

		// 通过voteId与userId获取用户的投票情况
		UserVoteVO userAnswerVO = voteService.getVoteAnswerForUser(userId,
				voteId);

		ModelAndView mv = new ModelAndView();
		ModelMap modelMap = mv.getModelMap();

		// 基本信息存放
		modelMap.put("info", userAnswerVO);

		// 存放距离截止时间
		buildLeftTime(userAnswerVO.getVoteVO(), modelMap);

		//这里要考虑对选项进行协同处理:
		//每个选项要包括：
		//选项描述，选项关键字，选项投票数，选项百分比，选项背景颜色，选项图标，是否被用户选中
		buildChoiceItemList(userAnswerVO,modelMap);
		
		
		mv.setViewName("voteDetail.html");
		return mv;
	}

	private void buildChoiceItemList(UserVoteVO userAnswerVO, ModelMap modelMap) {

		//首先准备好look方面的参数
		String lookConfig = userAnswerVO.getVoteVO().getLookConfig();
		lookConfig = lookConfig == null ? DEFAULT_LOOK : lookConfig;
		JSONObject lookJSON = JSONObject.parseObject(lookConfig);
		String[] bgColor = lookJSON.getString("choose-bg-color").split(",");
		String tintColor  = bgColor[0];
		String darkColor  = bgColor[1];
		JSONObject answerIconJSON = lookJSON.getJSONObject("answer-icon");
		
		//对ChoiceItem进行参数准备
		List<VoteChoiceItem> choiceItemList =  new ArrayList<>();
		double totalVoteCount = 0;
		List<AnswerResultEntry> answerList = userAnswerVO.getVoteVO().getVoteResult();
		for (AnswerResultEntry answerEntry : answerList) {
			VoteChoiceItem choiceItem = new VoteChoiceItem();
			choiceItem.setChoiceAnswer(answerEntry.getKey());
			choiceItem.setChoiceDiscription(answerEntry.getDiscription());
			totalVoteCount += answerEntry.getCount();
			choiceItem.setVoteCount(answerEntry.getCount());
			choiceItem.setIconUrl(PIC_CDN_URL+answerIconJSON.getString(answerEntry.getKey()));
			choiceItem.setUserAnswer(choiceItem.getChoiceAnswer().equals(userAnswerVO.getCurrentAnswer()));
			choiceItemList.add(choiceItem);
		}
		
		//构建百分比
		if(choiceItemList.size() == 2){
			VoteChoiceItem firstItem = choiceItemList.get(0);
			firstItem.setRateOfAll((int)(Math.rint((firstItem.getVoteCount() / totalVoteCount)*100)));
			VoteChoiceItem secondItem = choiceItemList.get(1);
			secondItem.setRateOfAll(100-firstItem.getRateOfAll());
			firstItem.setBgColor(tintColor);
			secondItem.setBgColor(tintColor);
			if(firstItem.getRateOfAll() > secondItem.getRateOfAll()){
				firstItem.setBgColor(darkColor);
				firstItem.setRateHighest(true);
			}else{
				secondItem.setBgColor(darkColor);
				secondItem.setRateHighest(true);
			}
		}else{
			for (VoteChoiceItem voteChoiceItem : choiceItemList) {
				voteChoiceItem.setRateOfAll((int)(Math.rint((voteChoiceItem.getVoteCount() / totalVoteCount)*100)));
			}
		}
		
		modelMap.put("choiceList", choiceItemList);
	}
	
	private void buildLeftTime(VoteVO voteVO, ModelMap modelMap) {
		
		//首先确定是否是当下正在进行的投票
		if(voteVO.getVoteId().equals(voteAnswerCache.getTotayVoteId())){
			//直接判断明天6点为结束时间
			//TODO 这种默认方式，还是需要优化，统一管理
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			cal.setTime(voteVO.getStartTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.add(Calendar.HOUR, 30);
			
			long leftMillis = cal.getTimeInMillis() - System.currentTimeMillis();
			
			int leftHour = (int)(leftMillis / MSECOND_PER_HOUR);
			if(leftHour > 0){
				modelMap.put("leftHour", leftHour);
			}else{
				long endTimeInHour = (int)(leftMillis/MSECOND_PER_MINUTE);
				modelMap.put("leftMinute", endTimeInHour);
			}
			
		}

	}

	/**
	 * 获取最近一定次数的投票信息列表
	 * 
	 * @param request
	 * @param response
	 * @param voteId
	 *            要查询的投票ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ModelAndView voteList(HttpServletRequest request,
			HttpServletResponse response, @RequestParam int recentRows)
			throws Exception {

		voteService.recentList(recentRows);

		// 直接去获取最近recentRows条投票记录信息
		/*
		 * 投票ID，问题，日期
		 */

		return new ModelAndView("tt.html");
	}

	@Autowired
	VoteService voteService;
	
	@Autowired
	VoteAnswerCache voteAnswerCache;
	
	private static final String PIC_CDN_URL = "http://o7pmdbbe0.bkt.clouddn.com/vote/icon/";
	
	private static final int MSECOND_PER_HOUR = 60 * 60 * 1000;
	
	private static final int MSECOND_PER_MINUTE = 60 * 1000;
}
