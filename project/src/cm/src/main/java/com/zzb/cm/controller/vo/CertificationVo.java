package com.zzb.cm.controller.vo;

import java.util.Map;

public class CertificationVo {

	private String id;
	private String agentid;
	/**
	 * 主营业务
	 */
	private String mainbiz;
	/**
	 * 用于获取图片信息
	 */
	private String agentnum;
	private String jobnum;
	private String tempjobnum;
	private String jobnumtype;
	/**
	 * 所在地区
	 */
	private String region;
	/**
	 * 机构id
	 */
	private String deptid;
	private String deptname;
	/**
	 * 渠道id
	 */
	private String channelid;
	private String channelname;
	/**
	 * 真实姓名
	 */
	private String name;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 身份证号
	 */
	private String idcard;
	/**
	 * 银行卡号 
	 */
	private String bankcard;
	private String belongs2bank;
	/**
	 * 资格证号
	 */
	private String cgfns;
	/**
	 * 0-初始 ；1-审核通过 ；2-审核不通过
	 */
	private int status;
	/**
	 * 正式工号
	 */
	private String formalnum;
	private String province;
	private String city;
	/**
	 * 给用户备注
	 */
	private String commentcontent;
	private Map<String, String> pic;
	/**
	 * 推荐人工号
	 */
	private String referrer;
	/**
	 * 推荐人姓名
	 */
	private String referrername;
	private String noti;
	private String designatedoperator;
	private String rank;

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getDesignatedoperator() {
		return designatedoperator;
	}

	public void setDesignatedoperator(String designatedoperator) {
		this.designatedoperator = designatedoperator;
	}

	public String getNoti() {
		return noti;
	}
	public void setNoti(String noti) {
		this.noti = noti;
	}
	public String getCommentcontent() {
		return commentcontent;
	}
	public void setCommentcontent(String commentcontent) {
		this.commentcontent = commentcontent;
	}
	public String getReferrer() {
		return referrer;
	}
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	public String getReferrername() {
		return referrername;
	}
	public void setReferrername(String referrername) {
		this.referrername = referrername;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	public String getTempjobnum() {
		return tempjobnum;
	}
	public void setTempjobnum(String tempjobnum) {
		this.tempjobnum = tempjobnum;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getJobnum() {
		return jobnum;
	}
	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}
	public String getJobnumtype() {
		return jobnumtype;
	}
	public void setJobnumtype(String jobnumtype) {
		this.jobnumtype = jobnumtype;
	}
	public String getFormalnum() {
		return formalnum;
	}
	public void setFormalnum(String formalnum) {
		this.formalnum = formalnum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMainbiz() {
		return mainbiz;
	}
	public void setMainbiz(String mainbiz) {
		this.mainbiz = mainbiz;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}
	public String getCgfns() {
		return cgfns;
	}
	public void setCgfns(String cgfns) {
		this.cgfns = cgfns;
	}
	public Map<String, String> getPic() {
		return pic;
	}
	public void setPic(Map<String, String> pic) {
		this.pic = pic;
	}
	public String getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}
	public String getAgentid() {
		return agentid;
	}
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	public String getBelongs2bank() {
		return belongs2bank;
	}
	public void setBelongs2bank(String belongs2bank) {
		this.belongs2bank = belongs2bank;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CertificationVo [id=" + id + ", agentid=" + agentid
				+ ", mainbiz=" + mainbiz + ", agentnum=" + agentnum
				+ ", jobnum=" + jobnum + ", tempjobnum=" + tempjobnum
				+ ", jobnumtype=" + jobnumtype + ", region=" + region
				+ ", deptid=" + deptid + ", deptname=" + deptname
				+ ", channelid=" + channelid + ", channelname=" + channelname
				+ ", name=" + name + ", mobile=" + mobile + ", idcard="
				+ idcard + ", bankcard=" + bankcard + ", belongs2bank="
				+ belongs2bank + ", cgfns=" + cgfns + ", status=" + status
				+ ", formalnum=" + formalnum + ", province=" + province
				+ ", city=" + city + ", pic=" + pic + "]";
	}
	
}
