package com.zzb.chn.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

/**
 * 渠道车辆信息查询记录表，按渠道ID分表，insbchncarqry_xxx，xxx为渠道ID
 * @author zwx
 *
 */
public class INSBChncarqry extends BaseEntity implements Identifiable { 
	private static final long serialVersionUID = 1L;
 
	private String channelinnercode; //渠道内部编码，即渠道ID
	private String carlicenseno; //车牌号
	private String idcardno; //车主身份证
	private String carowner; //车主
	private String taskid; //任务号
	private String jobnum; //出单工号

	public String getJobnum() {
		return jobnum;
	}

	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}

	public String getChannelinnercode() {
		return channelinnercode;
	}
	public void setChannelinnercode(String channelinnercode) {
		this.channelinnercode = channelinnercode;
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
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

}