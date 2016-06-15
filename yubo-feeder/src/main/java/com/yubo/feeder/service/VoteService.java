package com.yubo.feeder.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.feeder.dao.ResourceSvgMapper;
import com.yubo.feeder.dao.VoteBaseMapper;
import com.yubo.feeder.dao.pojo.ResourceSvg;
import com.yubo.feeder.dao.pojo.VoteBase;
import com.yubo.feeder.vo.DatagridResponse;
import com.yubo.feeder.vo.SvgVO;

/**
 * SVG CRUD服务
 * 
 * @author yangxy8
 *
 */
@Service
public class VoteService {

	public List<VoteBase> paging(Integer page, Integer row) {
		page = page == null ? 1 : page;
		row = row == null ? 10 : row;

		Map<String, Object> param = new HashMap<>();
		param.put("startRow", (page - 1) * row);
		param.put("rowCount", row);
		List<VoteBase> result = voteBaseMapper.selectByParam(param);
		return result;
	}
	
	
	public int getCountForAll(){
		return voteBaseMapper.countByParam(null);
	}
	
	public void add(SvgVO vo) {
//		ResourceSvg record = new ResourceSvg();
//		record.setSvgTag(vo.getSvgTag());
//		record.setSvgContent(vo.getSvgContent());
//		resourceSvgMapper.insertSelective(record);
	}
	

	public void update(SvgVO vo) {
//		ResourceSvg record = new ResourceSvg();
//		record.setSvgId(vo.getSvgId());
//		record.setSvgTag(vo.getSvgTag());
//		record.setSvgContent(vo.getSvgContent());
//		resourceSvgMapper.updateByPrimaryKeySelective(record);
	}

	@Autowired
	VoteBaseMapper voteBaseMapper;


}
