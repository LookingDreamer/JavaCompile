package com.zzb.mobile.model.policyoperat;

/**
 * liu
 * 查询保单列表接口使用
 */

public class PolicyitemQueryParam {

	/**
	 * 代理人工号
	 */
	private String agentnum;
	/**
	 * 车牌号
	 */
	private String carlicenseno;
	/**
	 * 被保人姓名
	 */
	private String insuredname;
	/**
	 * 模糊查询条件
	 */
	private String queryinfo;
	/**
	 * 每页显示条数
	 */
	private Integer limit;
	/**
	 * 加载偏移量
	 */
	private Long offset;
	/**
	 * 证件类型
	 */
	private Integer idcardtype;
	/**
	 * 证件号码
	 */
	private String idcardno;
	/**
	 * 标记cm或cif
	 */
	private String code;
	
	public String getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}
	public String getCarlicenseno() {
		return carlicenseno;
	}
	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
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
	public String getQueryinfo() {
		return queryinfo;
	}
	public void setQueryinfo(String queryinfo) {
		this.queryinfo = queryinfo;
	}
	public Integer getIdcardtype() {
		return idcardtype;
	}
	public void setIdcardtype(Integer idcardtype) {
		this.idcardtype = idcardtype;
	}
	public String getIdcardno() {
		return idcardno;
	}
	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
