package com.zzb.app.model;

/**
 * @author qiu
 *
 */
public class ShippingAddressInfoModel {
	
	/**
	 * 地址ID
	 */
	public String addressesid;
	/**
	 * 收货地址
	 */
	public String address;
	
	/**
	 * 是否默认地址
	 */
	public String isDefault;
	
	/**
	 * 省份编码
	 */
	public String  provinceId;
	
	/**
	 * 城市编码
	 */
	public String  cityId;
	
	/**
	 * 联系人名称
	 */
	public String  contactName;
	
	/**
	 * 邮编
	 */
	public String  postCode;
	
	/**
	 * 联系人ID
	 */
	public String contactInfoId;
	
	/**
	 * 联系电话
	 */
	public String  contactTel;
	
	/**
	 * 区县编码
	 */
	public String  zoneId;

	public String getAddressesid() {
		return addressesid;
	}

	public void setAddressesid(String addressesid) {
		this.addressesid = addressesid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getContactInfoId() {
		return contactInfoId;
	}

	public void setContactInfoId(String contactInfoId) {
		this.contactInfoId = contactInfoId;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	
}
