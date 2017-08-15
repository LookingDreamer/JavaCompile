package com.zzb.mobile.model;

import java.util.List;

/**
 * 协议信息展示
 * @author hejie
 *
 */
public class BranchProBean {

	/**
	 * id
	 */
	private String deptId;
	
	/**
	 * 供应商编码
	 */
	private String comcode;
	
	/**
	 * 供应商名称
	 */
	private String comname;
	/**
	 * 机构名称简称
	 */
	private String shortname;
	/**
	 * logo图标
	 */
	private String logo;
	
	/**
	 * 协议id
	 */
	private String agreementid;
	
	/**
	 * true 选中
	 */
	private boolean selected;
	
	/**
	 * 出单网点列表
	 */
	private List<SingleSiteBean> singleSiteBeans;
	
	/**
	 * true 匹配常用出单网点
	 */
	private boolean matchsite;
	/**
	 * 平台编码
	 */
	private String platform;
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public boolean isMatchsite() {
		return matchsite;
	}
	public void setMatchsite(boolean matchsite) {
		this.matchsite = matchsite;
	}
	public String getAgreementid() {
		return agreementid;
	}
	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public List<SingleSiteBean> getSingleSiteBeans() {
		return singleSiteBeans;
	}
	public void setSingleSiteBeans(List<SingleSiteBean> singleSiteBeans) {
		this.singleSiteBeans = singleSiteBeans;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getComcode() {
		return comcode;
	}
	public void setComcode(String comcode) {
		this.comcode = comcode;
	}
	public String getComname() {
		return comname;
	}
	public void setComname(String comname) {
		this.comname = comname;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
