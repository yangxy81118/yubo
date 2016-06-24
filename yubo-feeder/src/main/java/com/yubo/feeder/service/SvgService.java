package com.yubo.feeder.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.feeder.dao.ResourceSvgMapper;
import com.yubo.feeder.dao.pojo.ResourceSvg;
import com.yubo.feeder.vo.DatagridResponse;
import com.yubo.feeder.vo.Query;
import com.yubo.feeder.vo.SvgVO;
import com.yubo.wechat.support.EmptyChecker;
import com.yubo.wechat.support.PageUtil;

/**
 * SVG CRUD服务
 * 
 * @author yangxy8
 *
 */
@Service
public class SvgService {

	/**
	 * 分页查询 TODO 待优化
	 * @param page
	 * @param row
	 * @param word 查询的关键字，目前采用最简单的LIKE方法
	 * @return
	 */
	public DatagridResponse<SvgVO> paging(Query<SvgVO> query) {

		DatagridResponse<SvgVO> response = new DatagridResponse<>();
		query.setPage(query.getPage() == null? 1 : query.getPage());
		query.setRows(query.getRows() == null? 10 : query.getRows());

		Map<String, Object> param = new HashMap<>();
		param.put("startRow", (query.getPage() - 1) * query.getRows());
		param.put("rowCount", query.getRows());
		param.put("svgTag", query.getWord());
		param.put("sort", query.getSort());
		param.put("order", query.getOrder());
		
		List<ResourceSvg> result = resourceSvgMapper.paging(param);

		List<SvgVO> list = new ArrayList<>();
		for (ResourceSvg resourceSvg : result) {
			SvgVO vo = new SvgVO();
			vo.setSvgId(resourceSvg.getSvgId());
			vo.setSvgContent(resourceSvg.getSvgContent());
			vo.setSvgTag(resourceSvg.getSvgTag());
			list.add(vo);
		}

		int totalRow = resourceSvgMapper.countSelective(param);
		response.setRows(list);
		response.setTotal(totalRow);
		return response;
	}

	public void add(SvgVO vo) {
		ResourceSvg record = new ResourceSvg();
		record.setSvgTag(vo.getSvgTag());
		record.setSvgContent(vo.getSvgContent());
		resourceSvgMapper.insertSelective(record);
	}
	

	public void update(SvgVO vo) {
		ResourceSvg record = new ResourceSvg();
		record.setSvgId(vo.getSvgId());
		record.setSvgTag(vo.getSvgTag());
		record.setSvgContent(vo.getSvgContent());
		resourceSvgMapper.updateByPrimaryKeySelective(record);
	}

	@Autowired
	ResourceSvgMapper resourceSvgMapper;

	private static final int svgBatchRows = 10;
	
	public Map<Integer, String> getSvgDataList(Set<Integer> svgIdSet) {
		Map<Integer, String> result = new HashMap<>();
		int pageCount = PageUtil.pageCount(svgIdSet.size(), svgBatchRows);
		for (int i = 0; i < pageCount; i++) {
			List<Integer> thisBatchIds = getThisBatchId(svgIdSet);
			List<ResourceSvg> batchResult = resourceSvgMapper.searchBySvgId(thisBatchIds);
			appendResult(result,batchResult);
		}
		return result;
	}

	private void appendResult(Map<Integer, String> result,
			List<ResourceSvg> batchResult) {

		if(EmptyChecker.isEmpty(batchResult)){
			return;
		}
		
		for (ResourceSvg resourceSvg : batchResult) {
			result.put(resourceSvg.getSvgId(), resourceSvg.getSvgContent());
		}
	}

	private List<Integer> getThisBatchId(Set<Integer> svgIdSet) {
		List<Integer> thisBatchIds = new ArrayList<>();
		for (Integer id : svgIdSet) {
			thisBatchIds.add(id);
		}
		return thisBatchIds;
	}


}
