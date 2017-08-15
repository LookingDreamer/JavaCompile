package com.zzb.cm.controller.vo;

/**
 * @author xiaoyq
 *
 */
public class CertificationQueryVO {
	/**
	 * 简单查询车牌号或被保人
	 */
	private String idno;
	
	/**
	 * 任务状态
	 */
	private String status;
	
	/**
	 * 代理人姓名
	 */
	private String agentName;

	/**
	 * 代理人手机号
	 */
	private String agentphone;
	
	/**
	 * 主流程id
	 */
	private String province;

	private String city;
	/**
	 * 时间下限
	 */
	private String taskcreatetimeup;
	
	/**
	 * 时间上限
	 */
	private String taskcreatetimedown;
	
	/**
	 * 当前页码
	 */
	private Integer offset;

	private Integer currentpage;
	/**
	 * 每页条数
	 */
	private Integer limit;

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentphone() {
		return agentphone;
	}

	public void setAgentphone(String agentphone) {
		this.agentphone = agentphone;
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

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
}
