package com.zzb.conf.entity.baseData;

public class INSBQueryhistoryinfo {
	private static final long serialVersionUID = 1L;
	
	private String maininstanceid;//流程号
	private String carlicenseno;//车牌号
	private String bname;//被保人姓名
	private String taskcreatetimeup;//查询创建任务的开始时间
	private String taskcreatetimedown;//查询创建人物的终止时间
	private Integer currentpage;//第几页开始的条数
	private Integer limit;//每页的条数
	
	public String getMaininstanceid() {
		return maininstanceid;
	}
	public void setMaininstanceid(String maininstanceid) {
		this.maininstanceid = maininstanceid;
	}
	public String getCarlicenseno() {
		return carlicenseno;
	}
	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getTaskcreatetimeup() {
		return taskcreatetimeup;
	}
	public void setTaskcreatetimeup(String taskcreatetimeup) {
		this.taskcreatetimeup = taskcreatetimeup;
	}
	public String getTaskcreatetimedown() {
		return taskcreatetimedown;
	}
	public void setTaskcreatetimedown(String taskcreatetimedown) {
		this.taskcreatetimedown = taskcreatetimedown;
	}
	public Integer getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(Integer currentpage) {
		this.currentpage = currentpage;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	
	
	
}
