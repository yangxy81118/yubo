package com.yubo.feeder.vo;

/**
 * 查询通用VO
 * 
 * @author yangxy8
 *
 * @param <T>
 */
public class Query<T> {

	private Integer page;

	private Integer rows;

	private String word;

	private String sort;

	private String order;

	private T serviceVO;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public T getServiceVO() {
		return serviceVO;
	}

	public void setServiceVO(T serviceVO) {
		this.serviceVO = serviceVO;
	}

	@Override
	public String toString() {
		return "Query [page=" + page + ", rows=" + rows + ", word=" + word
				+ ", sort=" + sort + ", order=" + order + ", serviceVO="
				+ serviceVO + "]";
	}

}
