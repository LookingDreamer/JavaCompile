package com.zzb.model;


public class AppPaymentyzfModel {
	
/**
 * 是否为储蓄卡
 */
private String cashcardflag;
/**
 * 是否为信用卡
 */
private String creditcardflag;



public String getCashcardflag() {
	return cashcardflag;
}
public void setCashcardflag(String cashcardflag) {
	this.cashcardflag = cashcardflag;
}
public String getCreditcardflag() {
	return creditcardflag;
}
public void setCreditcardflag(String creditcardflag) {
	this.creditcardflag = creditcardflag;
}
@Override
public String toString() {
	return "AppPaymentyzfModel [cashcardflag=" + cashcardflag
			+ ", creditcardflag=" + creditcardflag + "]";
}



}
