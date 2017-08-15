package com.zzb.chn.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

/**
 * 渠道车辆信息查询次数统计表
 * @author zwx 
 *
 */
public class INSBChncarqrycount extends BaseEntity implements Identifiable { 
	private static final long serialVersionUID = 1L;
 
	private String channelinnercode; //渠道内部编码，即渠道ID 
	private Integer callcount; //一天内调用次数
	private String carlicenseno; //车牌号
	private String idcardno; //车主身份证
	private String carowner; //车主
	
	public String getChannelinnercode() {
		return channelinnercode;
	}
	public void setChannelinnercode(String channelinnercode) {
		this.channelinnercode = channelinnercode;
	}
	public Integer getCallcount() {
		return callcount;
	}
	public void setCallcount(Integer callcount) {
		this.callcount = callcount;
	}
	public String getCarlicenseno() {
		return carlicenseno;
	}
	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}
	public String getIdcardno() {
		return idcardno;
	}
	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}
	public String getCarowner() {
		return carowner;
	}
	public void setCarowner(String carowner) {
		this.carowner = carowner;
	}
	
}