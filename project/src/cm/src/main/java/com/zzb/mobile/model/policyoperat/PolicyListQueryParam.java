package com.zzb.mobile.model.policyoperat;

/**
 * liu
 * 投保前信息查询操作接口使用
 */

public class PolicyListQueryParam {

	/**
	 * 代理人工号
	 */
	private String agentnum;
	/**
	 * 模糊查询信息
	 */
	private String queryinfo;
	/**
	 * 每页条数
	 */
	private Integer pageSize;
	/**
	 * 当前页码
	 */
	private Integer currentPage;
	
	/**
	 * 查询类型
	 */
	private String querytype;
	
	public String getQuerytype() {
		return querytype;
	}
	public void setQuerytype(String querytype) {
		this.querytype = querytype;
	}
	public String getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}
	public String getQueryinfo() {
		return queryinfo;
	}
	public void setQueryinfo(String queryinfo) {
		this.queryinfo = queryinfo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	
}
