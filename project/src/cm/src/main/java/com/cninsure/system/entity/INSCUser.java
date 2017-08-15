package com.cninsure.system.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSCUser implements Identifiable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String operator;
	private Date createtime;
	private Date modifytime;
	private String noti;
	private String usercode;
	private String username;
	private String name;
	private String userorganization;
	private String groupid;
	private String password;
	private String picurl;
	private String sex;
	private String phone;
	private String email;	
	private String address;
	private String status;
	private String objectclass;
	private String userclass;
	private String rolenames;
	private String openid;
	private Integer onlinestatus;
	private int taskpool;
	private Date pwsmodifytime;
	/**
	 * 用户机构deptinnercode
	 */
	private String deptinnercode;

	public String getDeptinnercode() {
		return deptinnercode;
	}

	public void setDeptinnercode(String deptinnercode) {
		this.deptinnercode = deptinnercode;
	}

	public Date getPwsmodifytime() {
		return pwsmodifytime;
	}

	public void setPwsmodifytime(Date pwsmodifytime) {
		this.pwsmodifytime = pwsmodifytime;
	}

	public int getTaskpool() {
		return taskpool;
	}

	public void setTaskpool(int taskpool) {
		this.taskpool = taskpool;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public String getNoti() {
		return noti;
	}

	public void setNoti(String noti) {
		this.noti = noti;
	}

	/**
	 * 关联组织机构
	 */
	private String shortname;
	/**
	 * 关联组织机构
	 * 组织机构名称
	 */
	private String comname;
	/**
	 * 到期日期（默认2099年12月30日）
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private String maturitydata;
	public String getObjectclass() {
		return objectclass;
	}

	public void setObjectclass(String objectclass) {
		this.objectclass = objectclass;
	}

	public String getUserclass() {
		return userclass;
	}

	public void setUserclass(String userclass) {
		this.userclass = userclass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRolenames() {
		return rolenames;
	}

	public void setRolenames(String rolenames) {
		this.rolenames = rolenames;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserorganization() {
		return userorganization;
	}

	public void setUserorganization(String userorganization) {
		this.userorganization = userorganization;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	
	

	public String getMaturitydata() {
		return maturitydata;
	}

	public void setMaturitydata(String maturitydata) {
		this.maturitydata = maturitydata;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	
	

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public Integer getOnlinestatus() {
		return onlinestatus;
	}

	public void setOnlinestatus(Integer onlinestatus) {
		this.onlinestatus = onlinestatus;
	}

	@Override
	public String toString() {
		return "INSCUser [usercode=" + usercode + ", username=" + username
				+ ", name=" + name + ", userorganization=" + userorganization
				+ ", groupid=" + groupid + ", password=" + password
				+ ", picurl=" + picurl + ", sex=" + sex + ", phone=" + phone
				+ ", email=" + email + ", address=" + address + ", status="
				+ status + ", objectclass=" + objectclass + ", userclass="
				+ userclass + ", rolenames=" + rolenames + ", openid=" + openid
				+ ", onlinestatus=" + onlinestatus + ", shortname=" + shortname
				+ ", comname=" + comname + ", maturitydata=" + maturitydata
				+ ", pwsmodifytime=" + pwsmodifytime + "]";
	}


	

}
