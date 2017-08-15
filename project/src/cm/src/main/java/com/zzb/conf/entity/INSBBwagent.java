package com.zzb.conf.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBBwagent extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private String sex;
	
	/**
	 * 代理人编码或工号
	 */
	private String agentcode;
	
	/**
	 * 手机账号
	 */
	private String mobile;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 所属机构id  网点编码 
	 */
	private String mgmtdivision;
	
	/**
	 * 代理人级别
	 */
	private String agentgrade;
	
	/**
	 * 
	 * 代理人状态
	 */
	private String agentstate;
	
	/**
	 * 用户种类
	 */
	private String agentkind;
	
	/**
	 * 推荐人
	 */
	private String introagency;
	
	/**
	 * 资格证号
	 */
	private String quacertifno;
	
	/**
	 * 执业证/展业证号码
	 */
	private String pracertifno;
	
	/**
	 * 证件号码
	 */
	private String idno;
	
	/**
	 * 证件类别
	 */
	private String idnotype;
	
	/**
	 * 创建时间
	 */
	private Date maketime;
	
	/**
	 * 修改时间
	 */
	private Date modifytime;
	
	/**
	 * 居住地址
	 */
	private String homeaddress;
	
	/**
	 * 团队编码
	 */
	private String agentgroup;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAgentcode() {
		return agentcode;
	}

	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMgmtdivision() {
		return mgmtdivision;
	}

	public void setMgmtdivision(String mgmtdivision) {
		this.mgmtdivision = mgmtdivision;
	}

	public String getAgentgrade() {
		return agentgrade;
	}

	public void setAgentgrade(String agentgrade) {
		this.agentgrade = agentgrade;
	}

	public String getAgentstate() {
		return agentstate;
	}

	public void setAgentstate(String agentstate) {
		this.agentstate = agentstate;
	}

	public String getAgentkind() {
		return agentkind;
	}

	public void setAgentkind(String agentkind) {
		this.agentkind = agentkind;
	}

	public String getIntroagency() {
		return introagency;
	}

	public void setIntroagency(String introagency) {
		this.introagency = introagency;
	}

	public String getQuacertifno() {
		return quacertifno;
	}

	public void setQuacertifno(String quacertifno) {
		this.quacertifno = quacertifno;
	}

	public String getPracertifno() {
		return pracertifno;
	}

	public void setPracertifno(String pracertifno) {
		this.pracertifno = pracertifno;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getIdnotype() {
		return idnotype;
	}

	public void setIdnotype(String idnotype) {
		this.idnotype = idnotype;
	}

	public Date getMaketime() {
		return maketime;
	}

	public void setMaketime(Date maketime) {
		this.maketime = maketime;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getAgentgroup() {
		return agentgroup;
	}

	public void setAgentgroup(String agentgroup) {
		this.agentgroup = agentgroup;
	}

	@Override
	public String toString() {
		return "INSCBwagent [name=" + name + ", sex=" + sex + ", agentcode="
				+ agentcode + ", mobile=" + mobile + ", email=" + email
				+ ", mgmtdivision=" + mgmtdivision + ", agentgrade="
				+ agentgrade + ", agentstate=" + agentstate + ", agentkind="
				+ agentkind + ", introagency=" + introagency + ", quacertifno="
				+ quacertifno + ", pracertifno=" + pracertifno + ", idno="
				+ idno + ", idnotype=" + idnotype + ", maketime=" + maketime
				+ ", modifytime=" + modifytime + ", homeaddress=" + homeaddress
				+ ", agentgroup=" + agentgroup + "]";
	}

}
