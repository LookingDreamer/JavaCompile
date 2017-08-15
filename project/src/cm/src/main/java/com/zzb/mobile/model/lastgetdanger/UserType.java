package com.zzb.mobile.model.lastgetdanger;
public class UserType {
	String key; 
	String value;//"个人用车"
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "UserType [key=" + key + ", value=" + value + "]";
	}
	
}
