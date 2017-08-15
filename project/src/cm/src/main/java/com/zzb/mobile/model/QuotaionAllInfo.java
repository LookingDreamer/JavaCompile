package com.zzb.mobile.model;

import java.util.Date;
import java.util.List;

public class QuotaionAllInfo {

	/**
	 * 报价公司名称
	 */
	private String provname;
	/**
	 * 总保费
	 */
	private Double totalepremium;
	/**
	 * 商业险保费
	 */
	private Double sydiscountcharge;
	/**
	 * 商业险折扣率
	 */
	private Double sydiscountrate;
	/**
	 * 交强险保费
	 */
	private Double jqdiscountcharge;
	/**
	 * 交强险折扣率
	 */
	private Double jqdiscountrate;
	/**
	 * 车身浮动价
	 */
	private Double carbodyfloat;
	/**
	 * 商业险
	 */
	private List<CommerInsuranceBean> insuranceBeans;
	/**
	 * 交强险
	 */
	private List<StrongInsuranceBean> strongInsuranceBeans;

	private String syDate;

	private String jqDate;

	private boolean selected;

	public String getProvname() {
		return provname;
	}
	public void setProvname(String provname) {
		this.provname = provname;
	}
	public Double getTotalepremium() {
		return totalepremium;
	}
	public void setTotalepremium(Double totalepremium) {
		this.totalepremium = totalepremium;
	}
	public Double getSydiscountcharge() {
		return sydiscountcharge;
	}
	public void setSydiscountcharge(Double sydiscountcharge) {
		this.sydiscountcharge = sydiscountcharge;
	}
	public Double getSydiscountrate() {
		return sydiscountrate;
	}
	public void setSydiscountrate(Double sydiscountrate) {
		this.sydiscountrate = sydiscountrate;
	}
	public Double getJqdiscountcharge() {
		return jqdiscountcharge;
	}
	public void setJqdiscountcharge(Double jqdiscountcharge) {
		this.jqdiscountcharge = jqdiscountcharge;
	}
	public Double getJqdiscountrate() {
		return jqdiscountrate;
	}
	public void setJqdiscountrate(Double jqdiscountrate) {
		this.jqdiscountrate = jqdiscountrate;
	}
	public Double getCarbodyfloat() {
		return carbodyfloat;
	}
	public void setCarbodyfloat(Double carbodyfloat) {
		this.carbodyfloat = carbodyfloat;
	}
	public List<CommerInsuranceBean> getInsuranceBeans() {
		return insuranceBeans;
	}
	public void setInsuranceBeans(List<CommerInsuranceBean> insuranceBeans) {
		this.insuranceBeans = insuranceBeans;
	}
	public List<StrongInsuranceBean> getStrongInsuranceBeans() {
		return strongInsuranceBeans;
	}
	public void setStrongInsuranceBeans(
			List<StrongInsuranceBean> strongInsuranceBeans) {
		this.strongInsuranceBeans = strongInsuranceBeans;
	}

	public String getSyDate() {
		return syDate;
	}

	public void setSyDate(String syDate) {
		this.syDate = syDate;
	}

	public String getJqDate() {
		return jqDate;
	}

	public void setJqDate(String jqDate) {
		this.jqDate = jqDate;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
