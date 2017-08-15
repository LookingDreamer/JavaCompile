package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBProvider extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商编码
	 */
	private String prvcode;

	/**
	 * 名称
	 */
	private String prvname;

	/**
	 * 简称
	 */
	private String prvshotname;

	/**
	 * 级别
	 */
	private String prvgrade;

	/**
	 * 类型 01供应商02合作商
	 */
	private String prvtype;

	/**
	 * 网址
	 */
	private String prvurl;

	/**
	 * 联系人
	 */
	private String linkname;

	/**
	 * 联系方式
	 */
	private String linktel;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 省code
	 */
	private String province;

	/**
	 * 市code
	 */
	private String city;

	/**
	 * 县code
	 */
	private String county;
	
	/**
	 * 传真
	 */
	private String fax;
	
	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 客服电话
	 */
	private String servictel;

	/**
	 * 上级供应商code
	 */
	private String parentcode;

	/**
	 * 关联规则
	 */
	private String permissionorg;

	/**
	 * 归属机构
	 */
	private String affiliationorg;

	/**
	 * 业务类型 01传统02网销03电销
	 */
	private String businesstype;

	/**
	 * 渠道类型
	 */
	private String channeltype;

	/**
	 * logo
	 */
	private String logo;

	/**
	 * 公司介绍
	 */
	private String companyintroduce;

	/**
	 * 报价所需时间
	 */
	private Integer quotationtime;

	/**
	 * 两次报价间隔时间
	 */
	private Integer quotationinterval;

	/**
	 * 核保所需时间
	 */
	private Integer insuretime;

	/**
	 * 报价有效周期
	 */
	private Integer quotationvalidity;

	/**
	 * 支付号有效期
	 */
	private Integer payvalidity;

	/**
	 * 是否启用快速核保 1启用 0不启用
	 */
	private String quickinsureflag;

	/**
	 * 子节点标记  1为有子节点。0没有子节点
	 */
	private String childflag;

	/**
	 * eidid
	 */
	private String eidid;

	/**
	 * 接口类型
	 */
	private String interfacetype;

	/**
	 * 是否初审
	 */
	private Integer trialflag;

	/**
	 * 是否人工干预精报价结果
	 */
	private Integer priceflag;

	/**
	 * 是否核保失败人工干预
	 */
	private Integer underwritingflag;

	/**
	 * 是否启用外挂支持
	 */
	private Integer pluginflag;

	/**
	 * 排序（范围）
	 */
	private Integer orderflag;
	/**
	 * 允许提前【】个月报价
	 */
	private Integer advancequote;
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

	public String getPrvgrade() {
		return prvgrade;
	}

	public void setPrvgrade(String prvgrade) {
		this.prvgrade = prvgrade;
	}

	public String getPrvtype() {
		return prvtype;
	}

	public void setPrvtype(String prvtype) {
		this.prvtype = prvtype;
	}

	public String getPrvurl() {
		return prvurl;
	}

	public void setPrvurl(String prvurl) {
		this.prvurl = prvurl;
	}

	public String getLinkname() {
		return linkname;
	}

	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}

	public String getLinktel() {
		return linktel;
	}

	public void setLinktel(String linktel) {
		this.linktel = linktel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getServictel() {
		return servictel;
	}

	public void setServictel(String servictel) {
		this.servictel = servictel;
	}

	public String getParentcode() {
		return parentcode;
	}

	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}

	public String getPermissionorg() {
		return permissionorg;
	}

	public void setPermissionorg(String permissionorg) {
		this.permissionorg = permissionorg;
	}

	public String getAffiliationorg() {
		return affiliationorg;
	}

	public void setAffiliationorg(String affiliationorg) {
		this.affiliationorg = affiliationorg;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getChanneltype() {
		return channeltype;
	}

	public void setChanneltype(String channeltype) {
		this.channeltype = channeltype;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCompanyintroduce() {
		return companyintroduce;
	}

	public void setCompanyintroduce(String companyintroduce) {
		this.companyintroduce = companyintroduce;
	}

	public Integer getQuotationtime() {
		return quotationtime;
	}

	public void setQuotationtime(Integer quotationtime) {
		this.quotationtime = quotationtime;
	}

	public Integer getQuotationinterval() {
		return quotationinterval;
	}

	public void setQuotationinterval(Integer quotationinterval) {
		this.quotationinterval = quotationinterval;
	}

	public Integer getInsuretime() {
		return insuretime;
	}

	public void setInsuretime(Integer insuretime) {
		this.insuretime = insuretime;
	}

	public Integer getQuotationvalidity() {
		return quotationvalidity;
	}

	public void setQuotationvalidity(Integer quotationvalidity) {
		this.quotationvalidity = quotationvalidity;
	}

	public Integer getPayvalidity() {
		return payvalidity;
	}

	public void setPayvalidity(Integer payvalidity) {
		this.payvalidity = payvalidity;
	}

	public String getQuickinsureflag() {
		return quickinsureflag;
	}

	public void setQuickinsureflag(String quickinsureflag) {
		this.quickinsureflag = quickinsureflag;
	}

	public String getChildflag() {
		return childflag;
	}

	public void setChildflag(String childflag) {
		this.childflag = childflag;
	}

	public String getEidid() {
		return eidid;
	}

	public void setEidid(String eidid) {
		this.eidid = eidid;
	}

	public String getInterfacetype() {
		return interfacetype;
	}

	public void setInterfacetype(String interfacetype) {
		this.interfacetype = interfacetype;
	}

	public Integer getTrialflag() {
		return trialflag;
	}

	public void setTrialflag(Integer trialflag) {
		this.trialflag = trialflag;
	}

	public Integer getPriceflag() {
		return priceflag;
	}

	public void setPriceflag(Integer priceflag) {
		this.priceflag = priceflag;
	}

	public Integer getUnderwritingflag() {
		return underwritingflag;
	}

	public void setUnderwritingflag(Integer underwritingflag) {
		this.underwritingflag = underwritingflag;
	}

	public Integer getPluginflag() {
		return pluginflag;
	}

	public void setPluginflag(Integer pluginflag) {
		this.pluginflag = pluginflag;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(Integer orderflag) {
		this.orderflag = orderflag;
	}

	public Integer getAdvancequote() {
		return advancequote;
	}

	public void setAdvancequote(Integer advancequote) {
		this.advancequote = advancequote;
	}

	@Override
	public String toString() {
		return "INSBProvider [prvcode=" + prvcode + ", prvname=" + prvname
				+ ", prvshotname=" + prvshotname + ", prvgrade=" + prvgrade
				+ ", prvtype=" + prvtype + ", prvurl=" + prvurl + ", linkname="
				+ linkname + ", linktel=" + linktel + ", address=" + address
				+ ", province=" + province + ", city=" + city + ", county="
				+ county + ", fax=" + fax + ", email=" + email + ", servictel="
				+ servictel + ", parentcode=" + parentcode + ", permissionorg="
				+ permissionorg + ", affiliationorg=" + affiliationorg
				+ ", businesstype=" + businesstype + ", channeltype="
				+ channeltype + ", logo=" + logo + ", companyintroduce="
				+ companyintroduce + ", quotationtime=" + quotationtime
				+ ", quotationinterval=" + quotationinterval + ", insuretime="
				+ insuretime + ", quotationvalidity=" + quotationvalidity
				+ ", payvalidity=" + payvalidity + ", quickinsureflag="
				+ quickinsureflag + ", childflag=" + childflag + ", eidid="
				+ eidid + ", interfacetype=" + interfacetype + ", trialflag="
				+ trialflag + ", priceflag=" + priceflag
				+ ", underwritingflag=" + underwritingflag + ", pluginflag="
				+ pluginflag + ", orderflag=" + orderflag + ", advancequote="
				+ advancequote + "]";
	}

	
	
}