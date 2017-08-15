package com.zzb.mobile.model;

import net.sf.json.JSONArray;

import java.util.List;

public class ProviderListBean {

	/**
	 * id
	 */
	private String proId;
	/**
	 * 供应商编码
	 */
	private String prvcode;
	/**
	 * 供应商名称
	 */
	private String prvname;
	/**
	 * 供应商名称简称
	 */
	private String prvshotname;
	/**
	 * logo图标
	 */
	private String logo;
	/**
	 * 分公司列表
	 */
	private List<BranchProBean> branchProBeans;
	/**
	 * 渠道类型 0 传统 1 网销
	 */
	private String channeltype;
	/**
	 * true 选中
	 */
	private boolean isflag;
	/**
	 * 是否具有备用平台信息查询能力
	 */
	private String reservedPlatformresult;
	/**
	 * 0 自动(精灵，EDI) 1 人工
	 */
	private String aotohand; 
	/**
	 * 协议名称
	 */
	private String agreementname;
	/**
	 * 重复投保状态   0 都重复 1 交强险 商业险 有一个重复或者都不重复
	 */
	private int repeatinsured;
	/**
	 * true 是上年投保公司
	 */
	private boolean islastyearpro;

	private JSONArray ads;

	private Boolean isads;

	public Boolean getIsads() {
		return isads;
	}

	public void setIsads(Boolean isads) {
		this.isads = isads;
	}

	public JSONArray getAds() {
		return ads;
	}

	public void setAds(JSONArray ads) {
		this.ads = ads;
	}

	public boolean isIslastyearpro() {
		return islastyearpro;
	}

	public void setIslastyearpro(boolean islastyearpro) {
		this.islastyearpro = islastyearpro;
	}

	public int getRepeatinsured() {
		return repeatinsured;
	}

	public void setRepeatinsured(int repeatinsured) {
		this.repeatinsured = repeatinsured;
	}

	public String getAgreementname() {
		return agreementname;
	}

	public void setAgreementname(String agreementname) {
		this.agreementname = agreementname;
	}
	
	public String getReservedPlatformresult() {
		return reservedPlatformresult;
	}

	public void setReservedPlatformresult(String reservedPlatformresult) {
		this.reservedPlatformresult = reservedPlatformresult;
	}

	public String getAotohand() {
		return aotohand;
	}

	public void setAotohand(String aotohand) {
		this.aotohand = aotohand;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((proId == null) ? 0 : proId.hashCode());
		result = prime * result + ((prvcode == null) ? 0 : prvcode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProviderListBean other = (ProviderListBean) obj;
		if (proId == null) {
			if (other.proId != null)
				return false;
		} else if (!proId.equals(other.proId))
			return false;
		if (prvcode == null) {
			if (other.prvcode != null)
				return false;
		} else if (!prvcode.equals(other.prvcode))
			return false;
		return true;
	}

	public boolean isIsflag() {
		return isflag;
	}

	public void setIsflag(boolean isflag) {
		this.isflag = isflag;
	}

	public String getChanneltype() {
		return channeltype;
	}
	public void setChanneltype(String channeltype) {
		this.channeltype = channeltype;
	}
	public List<BranchProBean> getBranchProBeans() {
		return branchProBeans;
	}
	public void setBranchProBeans(List<BranchProBean> branchProBeans) {
		this.branchProBeans = branchProBeans;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getPrvcode() {
		return prvcode;
	}
	public void setPrvcode(String prvcode) {
		this.prvcode = prvcode;
	}
	public String getPrvname() {
		return prvname;
	}
	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}
	public String getPrvshotname() {
		return prvshotname;
	}
	public void setPrvshotname(String prvshotname) {
		this.prvshotname = prvshotname;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return "ProviderListBean [proId=" + proId + ", prvcode=" + prvcode
				+ ", prvname=" + prvname + ", prvshotname=" + prvshotname
				+ ", logo=" + logo + ", branchProBeans=" + branchProBeans
				+ ", channeltype=" + channeltype + ", isflag=" + isflag + "]";
	}
	
	
}
