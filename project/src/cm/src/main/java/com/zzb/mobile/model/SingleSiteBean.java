package com.zzb.mobile.model;
/**
 * 出单网点
 * @author hejie
 *
 */
public class SingleSiteBean {

	/**
	 * 网点id
	 */
	private String siteId;
	/**
	 * 网点名称
	 */
	private String siteName;
	/**
	 * 网点名称简称
	 */
	private String siteShortName;
	/**
	 * 网点地址
	 */
	private String siteAddress;
	/**
	 * 默认出单网点
	 */
	private boolean selected;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getSiteShortName() {
		return siteShortName;
	}

	public void setSiteShortName(String siteShortName) {
		this.siteShortName = siteShortName;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	
	
}
