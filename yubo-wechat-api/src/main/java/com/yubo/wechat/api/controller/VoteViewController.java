package com.yubo.wechat.api.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.impl.DefaultService;
import com.yubo.wechat.api.service.vo.MsgInputParam;
import com.yubo.wechat.api.xml.XMLHelper;
import com.yubo.wechat.api.xml.request.EventMsgRequest;
import com.yubo.wechat.user.service.UserService;
import com.yubo.wechat.vote.service.VoteRealTimeHandler;
import com.yubo.wechat.vote.service.VoteService;
import com.yubo.wechat.vote.service.vo.AnswerResultEntry;
import com.yubo.wechat.vote.service.vo.UserVoteVO;
import com.yubo.wechat.vote.service.vo.VoteHistoryVO;
import com.yubo.wechat.vote.service.vo.VoteVO;
import com.yubo.wechat.vote.util.VoteUtil;

/**
 * 投票展示信息入口
 * 
 * @author young.jason
 *
 */
@Controller
@RequestMapping("/vote")
public class VoteViewController extends BaseController{

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
			HttpServletResponse response, @RequestParam Long voteId,
			@RequestParam Integer userId) throws Exception {

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
		modelMap.put("startDate", buildStartDate(userAnswerVO.getVoteVO().getStartTime()));
		
		mv.setViewName("voteDetail.html");
		return mv;
	}

	private String buildStartDate(Date startTime) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		cal.setTime(startTime);
		String date = ""+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.DATE);
		return date;
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
		if(voteVO.getVoteId().equals(realTimeHandler.getActiveVoteId())){
			//直接判断明天6点为结束时间
			//TODO 这种默认方式，还是需要优化，统一管理
			Calendar cal = VoteUtil.getVoteEndTime(voteVO.getStartTime());
			
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
	 * @param req
	 * @param response
	 * @param voteId
	 *            要查询的投票ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ModelAndView voteList(HttpServletRequest req,
			HttpServletResponse response,@RequestParam Integer userId)
			throws Exception {
		
		// 直接去获取最近recentRows条投票记录信息
		List<VoteHistoryVO> voteStaticList = voteService.recentList(20);

		// 再去获取用户自己最近的投票记录
		if(userId!=null){
			Map<Long,UserVoteVO> userMap = voteService.getUserVoteList(20,userId);
			for (VoteHistoryVO voteHistoryVO : voteStaticList) {
				UserVoteVO userVote = userMap.get(voteHistoryVO.getVoteId());
				if(userVote!=null && userVote.getCurrentAnswer()!=null){
					voteHistoryVO.setUserChoice(userVote.getCurrentAnswer());
				}
				//顺便处理日期格式
				voteHistoryVO.setStartDateForView(buildStartDate(voteHistoryVO.getStartTime()));
			}
		}
		
		ModelAndView mv = new ModelAndView("voteList.html");
		ModelMap modelMap = mv.getModelMap();
		modelMap.put("voteList", voteStaticList);
		modelMap.put("userId", userId);
		
		return mv;
	}

	@Autowired
	VoteService voteService;
	
	@Autowired
	VoteRealTimeHandler realTimeHandler;
	
	@Autowired
	UserService userService;
	
	private static final String PIC_CDN_URL = "http://o7pmdbbe0.bkt.clouddn.com/vote/icon/";
	
	private static final int MSECOND_PER_HOUR = 60 * 60 * 1000;
	
	private static final int MSECOND_PER_MINUTE = 60 * 1000;
}
