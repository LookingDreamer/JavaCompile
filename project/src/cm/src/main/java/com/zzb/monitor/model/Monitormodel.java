package com.zzb.monitor.model;

public class Monitormodel {

	private String id;
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 接口地址
	 */
	private String interfaceaddress;
	
	/**
	 * 所属机构
	 */
	private String orgcode;
	
	/**
	 * 所属机构名称
	 */
	private String orgname;
	/**
	 * 健康状态 0-健康，1-异常
	 */
	private String status;
	/**
	 * 总数
	 */
	private String total;
	/**
	 * 成功数
	 */
	private String suc;
	/**
	 * 平均用时
	 */
	private String avg;
	
	/**
	 * 精灵edi
	 */
	private String quotetype;
	
	/**
	 * 是否当天，true  false
	 */
	private String todayflag;
	
	public String getTodayflag() {
		return todayflag;
	}

	public void setTodayflag(String todayflag) {
		this.todayflag = todayflag;
	}

	public String getQuotetype() {
		return quotetype;
	}

	public void setQuotetype(String quotetype) {
		this.quotetype = quotetype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInterfaceaddress() {
		return interfaceaddress;
	}

	public void setInterfaceaddress(String interfaceaddress) {
		this.interfaceaddress = interfaceaddress;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSuc() {
		return suc;
	}

	public void setSuc(String suc) {
		this.suc = suc;
	}

	public String getAvg() {
		return avg;
	}

	public void setAvg(String avg) {
		this.avg = avg;
	}
	
}
