package com.yubo.wechat.content.service.textWorker;

import java.util.ArrayList;
import java.util.List;

import com.yubo.wechat.content.vo.MessageVO;

/**
 * 内容池<br/>
 * 同时间段的内容会放在一个池内<br/>
 * 每天进行一次重新加载
 * 
 * @author yangxy8
 *
 */
public class TextPool {

	private Integer startHour;

	private Integer endHour;

	private List<MessageVO> textContent = new ArrayList<>();

	private Integer periodId;
	
	/**
	 * TextGuide进行随机数定位时，本TextPool所占的区间范围的起始数值
	 */
	private Double accessRdmStart;

	private Double accessRdmEnd;
	
	public Double getAccessRdmStart() {
		return accessRdmStart;
	}

	public void setAccessRdmStart(Double accessRdmStart) {
		this.accessRdmStart = accessRdmStart;
	}

	public Double getAccessRdmEnd() {
		return accessRdmEnd;
	}

	public void setAccessRdmEnd(Double accessRdmEnd) {
		this.accessRdmEnd = accessRdmEnd;
	}

	public Integer getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
	}

	public List<MessageVO> getTextContent() {
		return textContent;
	}

	public void setTextContent(List<MessageVO> textContent) {
		this.textContent = textContent;
	}

	public Integer getStartHour() {
		return startHour;
	}

	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}

	public Integer getEndHour() {
		return endHour;
	}

	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}

}
