package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBBaseData extends BaseEntity implements Identifiable  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 必填
	 *TODO 机构编码(存的是数字但是没有位数限制暂定用 varchar)
	 */
	private String basedatacode;
	/**
	 * 上级机构编码
	 */
	private String parentbasedatacode;
	
	/**
	 * 必填
	 * 名称
	 */
	private String basedatacodename;
	/**
	 * 必填
	 * 简称
	 */
	private String shortname;
	/**
	 * 地址
	 */
	private String address;
	/**
	 *TODO 邮政编码(国内邮编都是数字(用int性能比较好)，国外其他地区不定 )
	 */
	private String postalcode;
	/**
	 * 必填
	 * 省份
	 */
	private Integer provincecode;
	/**
	 * 必填
	 * 城市
	 */
	private Integer citycode;
	/**
	 * 关联
	 */
	private String contact;
	/**
	 * TODO电话(手机座机都有 暂用 变长varchar)
	 */
	private String phone;
	/**
	 * 传真
	 */
	private String fax;
	/**
	 * 邮件
	 */
	private String email;
	/**
	 * 网站
	 */
	private String website;
	
	/**
	 * 影像OCR识别
	 */
	private Integer ocrrecogniz;
	/**
	 * 影像人工识别
	 */
	private Integer artificialrecogniz;
	public String getBasedatacode() {
		return basedatacode;
	}
	public void setBasedatacode(String basedatacode) {
		this.basedatacode = basedatacode;
	}
	public String getParentbasedatacode() {
		return parentbasedatacode;
	}
	public void setParentbasedatacode(String parentbasedatacode) {
		this.parentbasedatacode = parentbasedatacode;
	}
	public String getBasedatacodename() {
		return basedatacodename;
	}
	public void setBasedatacodename(String basedatacodename) {
		this.basedatacodename = basedatacodename;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public Integer getProvincecode() {
		return provincecode;
	}
	public void setProvincecode(Integer provincecode) {
		this.provincecode = provincecode;
	}
	public Integer getCitycode() {
		return citycode;
	}
	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public Integer getOcrrecogniz() {
		return ocrrecogniz;
	}
	public void setOcrrecogniz(Integer ocrrecogniz) {
		this.ocrrecogniz = ocrrecogniz;
	}
	public Integer getArtificialrecogniz() {
		return artificialrecogniz;
	}
	public void setArtificialrecogniz(Integer artificialrecogniz) {
		this.artificialrecogniz = artificialrecogniz;
	}
	@Override
	public String toString() {
		return "INSBBaseData [basedatacode=" + basedatacode
				+ ", parentbasedatacode=" + parentbasedatacode
				+ ", basedatacodename=" + basedatacodename + ", shortname="
				+ shortname + ", address=" + address + ", postalcode="
				+ postalcode + ", provincecode=" + provincecode + ", citycode="
				+ citycode + ", contact=" + contact + ", phone=" + phone
				+ ", fax=" + fax + ", email=" + email + ", website=" + website
				+ ", ocrrecogniz=" + ocrrecogniz + ", artificialrecogniz="
				+ artificialrecogniz + "]";
	}
	
	
}
