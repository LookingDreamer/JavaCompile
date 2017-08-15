package com.zzb.cm.controller.vo;

public class DeliveryInfoItem {
	private String taskid;
	private String inscomcode;
	private String orderfrom;
	private String deliveryside;
	private String recipientname;
	private String recipientmobilephone;
	private String recipientaddress;
	private String zip;
	private String province;
	private String city;
	private String area;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getOrderfrom() {
		return orderfrom;
	}
	public void setOrderfrom(String orderfrom) {
		this.orderfrom = orderfrom;
	}
	public String getDeliveryside() {
		return deliveryside;
	}
	public void setDeliveryside(String deliveryside) {
		this.deliveryside = deliveryside;
	}
	public String getRecipientname() {
		return recipientname;
	}
	public void setRecipientname(String recipientname) {
		this.recipientname = recipientname;
	}
	public String getRecipientmobilephone() {
		return recipientmobilephone;
	}
	public void setRecipientmobilephone(String recipientmobilephone) {
		this.recipientmobilephone = recipientmobilephone;
	}
	public String getRecipientaddress() {
		return recipientaddress;
	}
	public void setRecipientaddress(String recipientaddress) {
		this.recipientaddress = recipientaddress;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
}
