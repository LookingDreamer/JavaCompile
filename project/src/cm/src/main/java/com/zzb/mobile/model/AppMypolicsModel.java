package com.zzb.mobile.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AppMypolicsModel {

private String id;                   //投保单id

private String carlicenseno;        //车牌号
	
private String carownername;	    //车主姓名
@DateTimeFormat(pattern="yyyy-MM-dd")
private String insureddate;          //投保日期
           


@Override
public String toString() {
	return "AppMypolicsModel [id=" + id + ", carlicenseno=" + carlicenseno
			+ ", carownername=" + carownername + ", insureddate=" + insureddate
			+ "]";
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getCarlicenseno() {
	return carlicenseno;
}

public void setCarlicenseno(String carlicenseno) {
	this.carlicenseno = carlicenseno;
}

public String getCarownername() {
	return carownername;
}

public void setCarownername(String carownername) {
	this.carownername = carownername;
}

public String getInsureddate() {
	return insureddate;
}

public void setInsureddate(String insureddate) {
	this.insureddate = insureddate;
}



}
