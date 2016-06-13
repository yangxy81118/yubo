package com.yubo.feeder.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.feeder.dao.ResourceSvgMapper;
import com.yubo.feeder.dao.pojo.ResourceSvg;
import com.yubo.feeder.vo.SvgVO;

/**
 * SVG CRUD服务
 * 
 * @author yangxy8
 *
 */
@Service
public class SvgService {

	public List<SvgVO> paging(Integer page, Integer row) {

		page = page == null ? 1 : page;
		row = row == null ? 10 : row;

		Map<String, Object> param = new HashMap<>();
		param.put("startRow", (page - 1) * row);
		param.put("rowCount", row);
		List<ResourceSvg> result = resourceSvgMapper.paging(param);

		List<SvgVO> list = new ArrayList<>();
		for (ResourceSvg resourceSvg : result) {
			SvgVO vo = new SvgVO();
			vo.setSvgId(resourceSvg.getSvgId());
			vo.setSvgContent(resourceSvg.getSvgContent());
			vo.setSvgTag(resourceSvg.getSvgTag());
			list.add(vo);
		}
		return list;
	}

	public void add(SvgVO vo) {
		ResourceSvg record = new ResourceSvg();
		record.setSvgTag(vo.getSvgTag());
		record.setSvgContent(vo.getSvgContent());
		resourceSvgMapper.insertSelective(record);
	}

	@Autowired
	ResourceSvgMapper resourceSvgMapper;

}
