package com.zzb.model;

import java.util.Date;
import java.util.List;

import com.zzb.conf.entity.INSBAgent;


public class INSBAgentQueryModel extends INSBAgent {



	/**
	 * 
	 */
	private static final long serialVersionUID = -4837025805417303729L;


	/**
	 * 注册日期止
	 */
	private Date registertimeend;
	
	
	private String registertimeendstr;

	/**
	 * 验证日期止
	 */
	private Date testtimeend;
	
	
	private String testtimeendstr;
	
	private List<String> deptWDids;

	public Date getRegistertimeend() {
		return registertimeend;
	}

	public void setRegistertimeend(Date registertimeend) {
		this.registertimeend = registertimeend;
	}

	public Date getTesttimeend() {
		return testtimeend;
	}

	public void setTesttimeend(Date testtimeend) {
		this.testtimeend = testtimeend;
	}

	public String getRegistertimeendstr() {
		return registertimeendstr;
	}

	public void setRegistertimeendstr(String registertimeendstr) {
		this.registertimeendstr = registertimeendstr;
	}

	public String getTesttimeendstr() {
		return testtimeendstr;
	}

	public void setTesttimeendstr(String testtimeendstr) {
		this.testtimeendstr = testtimeendstr;
	}

	public List<String> getDeptWDids() {
		return deptWDids;
	}

	public void setDeptWDids(List<String> deptWDids) {
		this.deptWDids = deptWDids;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}