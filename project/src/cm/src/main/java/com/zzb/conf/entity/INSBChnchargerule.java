package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

/**
 * 渠道接口收费规则表
 * @author zwx 
 *
 */
public class INSBChnchargerule extends BaseEntity implements Identifiable { 
	private static final long serialVersionUID = 1L;
 
	private String agreementinterfaceid; //渠道协议接口id
	private String charge;               //收费
	private String converstart;          //转化率开始
	private String converstartcompar;    //转化率开始符号
	private String converend;            //转化率结束
	private String converendcompar;      //转化率结束符号
	private String conver;
	
	public String getAgreementinterfaceid() {
		return agreementinterfaceid;
	}
	public void setAgreementinterfaceid(String agreementinterfaceid) {
		this.agreementinterfaceid = agreementinterfaceid;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getConverstart() {
		return converstart;
	}
	public void setConverstart(String converstart) {
		this.converstart = converstart;
	}
	public String getConverstartcompar() {
		return converstartcompar;
	}
	public void setConverstartcompar(String converstartcompar) {
		this.converstartcompar = converstartcompar;
	}
	public String getConverend() {
		return converend;
	}
	public void setConverend(String converend) {
		this.converend = converend;
	}
	public String getConverendcompar() {
		return converendcompar;
	}
	public void setConverendcompar(String converendcompar) {
		this.converendcompar = converendcompar;
	}
	public String getConver() {
		return conver;
	}
	public void setConver(String conver) {
		this.conver = conver;
	}
	
}