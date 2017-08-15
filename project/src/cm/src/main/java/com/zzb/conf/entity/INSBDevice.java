package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBDevice extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 代理人id
	 */
	private String agentid;

	/**
	 * 机构表id
	 */
	private String deptid;

	/**
	 * 授权码
	 */
	private String authcode;

	/**
	 * 授权码类型
	 */
	private String authtype;

	/**
	 * 掌中宝绑定状态
	 */
	private String bindingstate;

	/**
	 * 首次激活时间
	 */
	private Date activatetime;

	/**
	 * 是否允许所有代理人登录
	 */
	private String agentflag;

	/**
	 * 所属机构层级
	 */
	private String depttier;

	/**
	 * 设备品牌
	 */
	private String devbrand;

	/**
	 * 设备型号
	 */
	private String devtype;

	/**
	 * IMEI/SN
	 */
	private String imei;

	/**
	 * 序列号
	 */
	private String serialnum;

	/**
	 * 操作系统
	 */
	private String opersystem;

	/**
	 * 设备来源
	 */
	private String devsource;

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public String getAuthtype() {
		return authtype;
	}

	public void setAuthtype(String authtype) {
		this.authtype = authtype;
	}

	public String getBindingstate() {
		return bindingstate;
	}

	public void setBindingstate(String bindingstate) {
		this.bindingstate = bindingstate;
	}

	public Date getActivatetime() {
		return activatetime;
	}

	public void setActivatetime(Date activatetime) {
		this.activatetime = activatetime;
	}

	public String getAgentflag() {
		return agentflag;
	}

	public void setAgentflag(String agentflag) {
		this.agentflag = agentflag;
	}

	public String getDepttier() {
		return depttier;
	}

	public void setDepttier(String depttier) {
		this.depttier = depttier;
	}

	public String getDevbrand() {
		return devbrand;
	}

	public void setDevbrand(String devbrand) {
		this.devbrand = devbrand;
	}

	public String getDevtype() {
		return devtype;
	}

	public void setDevtype(String devtype) {
		this.devtype = devtype;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(String serialnum) {
		this.serialnum = serialnum;
	}

	public String getOpersystem() {
		return opersystem;
	}

	public void setOpersystem(String opersystem) {
		this.opersystem = opersystem;
	}

	public String getDevsource() {
		return devsource;
	}

	public void setDevsource(String devsource) {
		this.devsource = devsource;
	}

}