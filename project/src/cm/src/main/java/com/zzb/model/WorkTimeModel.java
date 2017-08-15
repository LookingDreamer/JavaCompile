package com.zzb.model;

import java.util.List;

import com.zzb.conf.entity.INSBHolidayarea;
import com.zzb.conf.entity.INSBWorktimearea;

public class WorkTimeModel {
	
	/**
	 * worktimeid
	 */
	private String insbworktimeid;
	/**
	 * 机构id
	 */
	private String inscdeptid;
	/**
	 * 机构名称
	 */
	private String inscdeptName;
	/**
	 * 支付时间类型
	 */
	private String paytimetype;

	/**
	 * 非工作时间文字提醒
	 */
	private String noworkwords;

	/**
	 * 工作时间文字提醒
	 */
	private String workwords;
	/**
	 * 工作时间list
	 */
	private List<INSBWorktimearea> workTimeArea;
	/**
	 * 工作时间提醒list
	 */
	private List<INSBWorktimearea> remindWorkTimeArea;
	/**
	 * 节假日list
	 */
	private List<INSBHolidayarea> holidayArea;
	/**
	 * 节假日值班日list
	 */
	private List<INSBHolidayarea> holidayWorkArea;
	
	/**
	 * 网点禁用状态
	 */
	private String networkstate;
	
	public String getInsbworktimeid() {
		return insbworktimeid;
	}
	public void setInsbworktimeid(String insbworktimeid) {
		this.insbworktimeid = insbworktimeid;
	}
	public String getInscdeptid() {
		return inscdeptid;
	}
	public void setInscdeptid(String inscdeptid) {
		this.inscdeptid = inscdeptid;
	}
	public String getInscdeptName() {
		return inscdeptName;
	}
	public void setInscdeptName(String inscdeptName) {
		this.inscdeptName = inscdeptName;
	}
	public String getPaytimetype() {
		return paytimetype;
	}
	public void setPaytimetype(String paytimetype) {
		this.paytimetype = paytimetype;
	}
	public String getNoworkwords() {
		return noworkwords;
	}
	public void setNoworkwords(String noworkwords) {
		this.noworkwords = noworkwords;
	}
	public String getWorkwords() {
		return workwords;
	}
	public void setWorkwords(String workwords) {
		this.workwords = workwords;
	}
	public List<INSBWorktimearea> getWorkTimeArea() {
		return workTimeArea;
	}
	public void setWorkTimeArea(List<INSBWorktimearea> workTimeArea) {
		this.workTimeArea = workTimeArea;
	}
	public List<INSBWorktimearea> getRemindWorkTimeArea() {
		return remindWorkTimeArea;
	}
	public void setRemindWorkTimeArea(List<INSBWorktimearea> remindWorkTimeArea) {
		this.remindWorkTimeArea = remindWorkTimeArea;
	}
	public List<INSBHolidayarea> getHolidayArea() {
		return holidayArea;
	}
	public void setHolidayArea(List<INSBHolidayarea> holidayArea) {
		this.holidayArea = holidayArea;
	}
	public List<INSBHolidayarea> getHolidayWorkArea() {
		return holidayWorkArea;
	}
	public void setHolidayWorkArea(List<INSBHolidayarea> holidayWorkArea) {
		this.holidayWorkArea = holidayWorkArea;
	}
	public String getNetworkstate() {
		return networkstate;
	}
	public void setNetworkstate(String networkstate) {
		this.networkstate = networkstate;
	}
	
	
	
}
