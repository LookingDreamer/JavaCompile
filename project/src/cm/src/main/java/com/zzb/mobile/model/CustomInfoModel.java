package com.zzb.mobile.model;

import java.io.Serializable;
import java.util.Date;

public class CustomInfoModel implements Serializable{

	private String customerName;//客户名称
	private String customerIdCardTypeName;//证件类型
	private String customerIdCardNo; //证件号码
	private int carNum;//车辆数量
	private String carPate;//车牌号
	private String carModel;//车型
	private String gender;
	private String birthday; 
	private int age;
	private String customerIdCardType;
	private String cellphone;
	private String email;
	private String address;
	
	
	

	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getCarPate() {
		return carPate;
	}
	public void setCarPate(String carPate) {
		this.carPate = carPate;
	}
	public String getCustomerIdCardTypeName() {
		return customerIdCardTypeName;
	}
	public void setCustomerIdCardTypeName(String customerIdCardTypeName) {
		this.customerIdCardTypeName = customerIdCardTypeName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerIdCardType() {
		return customerIdCardType;
	}
	public void setCustomerIdCardType(String customerIdCardType) {
		this.customerIdCardType = customerIdCardType;
	}
	public String getCustomerIdCardNo() {
		return customerIdCardNo;
	}
	public void setCustomerIdCardNo(String customerIdCardNo) {
		this.customerIdCardNo = customerIdCardNo;
	}
	public int getCarNum() {
		return carNum;
	}
	public void setCarNum(int carNum) {
		this.carNum = carNum;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "CustomInfoModel [customerName=" + customerName
				+ ", customerIdCardType=" + customerIdCardType
				+ ", customerIdCardNo=" + customerIdCardNo + ", carNum="
				+ carNum + ", gender=" + gender + ", birthday=" + birthday
				+ ", age=" + age + ", cellphone=" + cellphone + ", email="
				+ email + ", address=" + address + "]";
	}



	
}
