package com.yubo.feeder.vo;

import java.util.List;

/**
 * EasyUI Datagrid组件需要的数据结构
 * 
 * @author yangxy8
 *
 * @param <T>
 */
public class DatagridResponse<T> {

	private Integer total;

	private List<T> rows;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "DatagridResponse [total=" + total + ", rows=" + rows + "]";
	}
}
