package com.common;

/**
 * 公共的分页参数类
 * 
 * @version 1.0
 */
public class PagingParams {

	/**
	 * 每页显示条数
	 */
	private Integer limit;
	/**
	 * limit offset, limit;
	 */
	private Long offset;
	/**
	 * 搜索
	 */
	private String search;
	/**
	 * 排序字段名
	 */
	private String sort;
	/**
	 * asc or desc
	 */
	private String order;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
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

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "PagingParams [limit=" + limit + ", offset=" + offset
				+ ", search=" + search + ", sort=" + sort + ", order=" + order
				+ "]";
	}

}
