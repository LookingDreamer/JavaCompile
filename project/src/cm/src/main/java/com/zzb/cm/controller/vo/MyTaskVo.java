package com.zzb.cm.controller.vo;

public class MyTaskVo {
	private String createby;
	private String deptid;
	private String carlicenseno;
	private String jobnumtype;//工号类型	1-临时工号	 2-正式工号
	private String startdate;
	private String enddate;
	private String insuredname;
	private String taskcode;//7-规则报价,8-人工调整,人工录单,精灵录单,18-二次支付,认证
	private String taskstatus;
	private String agentname;
	private String agentnum;
	private String simplequery;
	private int currentpage;//当前页 
	private long totals;//总条数
	private int pagesize;//每页条数
	private int totalpage;//总页数
	private String deptinnercode;
	
	public String getDeptinnercode() {
		return deptinnercode;
	}
	public void setDeptinnercode(String deptinnercode) {
		this.deptinnercode = deptinnercode;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public String getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getCarlicenseno() {
		return carlicenseno;
	}
	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}
	public String getJobnumtype() {
		return jobnumtype;
	}
	public void setJobnumtype(String jobnumtype) {
		this.jobnumtype = jobnumtype;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	public String getTaskcode() {
		return taskcode;
	}
	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}
	public String getTaskstatus() {
		return taskstatus;
	}
	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}
	public String getSimplequery() {
		return simplequery;
	}
	public void setSimplequery(String simplequery) {
		this.simplequery = simplequery;
	}
	public int getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
	public long getTotals() {
		return totals;
	}
	public void setTotals(long totals) {
		this.totals = totals;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	
}
