package com.zzb.mobile.model;

public class SearchCarModel {

	/**
	 * 车型名称
	 */
	private String modelname;
	/**
	 * 每页显示条数
	 */
	private String pagesize;
	/**
	 * 当前第几页
	 */
	private String currentpage;
	/**
	 * 车牌号
	 */
	private String carlicenseno;
	private String agentid;

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getCarlicenseno() {
		return carlicenseno;
	}
	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}
	public String getModelname() {
		return modelname;
	}
	public void setModelname(String modelname) {
		this.modelname = modelname;
	}
	public String getPagesize() {
		return pagesize;
	}
	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}
	public String getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(String currentpage) {
		this.currentpage = currentpage;
	}
	
	
}
