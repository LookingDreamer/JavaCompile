package com.zzb.extra.model;
/**
 * 
 * @author shiguiwu
 * @date 2017年3月1日
 */
public class MiniUserOrderQueryModel {

	private String taskid;
	
	private String agentid;
	
	private String prvid;
	
	private String carlicenseno;
	
	private String prvname;
	
	private String inscomcode;
	
	private String prvcode;
	
	private String pageNum;

	private String pageSize;
	
	private Double totalpaymentamount;

	public Double getTotalpaymentamount() {
		return totalpaymentamount;
	}

	public void setTotalpaymentamount(Double totalpaymentamount) {
		this.totalpaymentamount = totalpaymentamount;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getPrvid() {
		return prvid;
	}

	public void setPrvid(String prvid) {
		this.prvid = prvid;
	}

	public String getCarlicenseno() {
		return carlicenseno;
	}

	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}

	public String getPrvname() {
		return prvname;
	}

	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getPrvcode() {
		return prvcode;
	}

	public void setPrvcode(String prvcode) {
		this.prvcode = prvcode;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}


	
}
