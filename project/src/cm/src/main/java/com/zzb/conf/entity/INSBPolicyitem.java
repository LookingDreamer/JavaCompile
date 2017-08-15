package com.zzb.conf.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBPolicyitem extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 订单表id
	 */
	private String orderid;

	/**
	 * 投保人id
	 */
	private String applicantid;

	/**
	 * 投保人姓名
	 */
	private String applicantname;

	/**
	 * 被保人id
	 */
	private String insuredid;

	/**
	 * 被保人姓名
	 */
	private String insuredname;

	/**
	 * 车主id
	 */
	private String carownerid;

	/**
	 * 车主姓名
	 */
	private String carownername;
	private String shouyirenname;//受益人姓名
	private String beibaorenname;//被保人姓名
	private String toubaorenname;//投保人姓名
	private String toubaoidcardno;//投保人证件号
	private String contactsname;//联系人

	/**
	 * 车辆信息id
	 */
	private String carinfoid;

	/**
	 * 车型信息描述
	 */
	private String standardfullname;

	/**
	 * 投保单号
	 */
	private String proposalformno;

	/**
	 * 保单号
	 */
	private String policyno;

	/**
	 * 保费
	 */
	private Double premium;

	/**
	 * 承保日期
	 */
	private Date insureddate;

	/**
	 * 起保日期
	 */
	private Date startdate;
	private String startdates;

	/**
	 * 终止日期
	 */
	private Date enddate;
	private String enddates;

	/**
	 * 总保费
	 */
	private Double totalepremium;

	/**
	 * 代理人工号
	 */
	private String agentnum;

	/**
	 * 代理人名称
	 */
	private String agentname;

	/**
	 * 所属团队
	 */
	private String team;

	/**
	 * 保单状态
	 */
	private String policystatus;
	
	/**
	 * 关闭状态 0 未关闭 1 已关闭
	 */
	private String closedstatus;

	/**
	 * 险种类型 0 商业险 1 交强险
	 */
	private String risktype;

	/**
	 * 
	 */
	private String paynum;

	/**
	 * 暂存单号
	 */
	private String checkcode;

	/**
	 * 折后保费
	 */
	private Double discountCharge;

	/**
	 * 折扣率
	 */
	private Double discountRate;

	/**
	 * 保额
	 */
	private Double amount;
	
	/**
	 * 保险公司code
	 */
	private String inscomcode;
	
	private String biztype;
	/**
	 * 电子保单
	 */
	private String elecpolicy;
	

	public String getElecpolicy() {
		return elecpolicy;
	}

	public void setElecpolicy(String elecpolicy) {
		this.elecpolicy = elecpolicy;
	}

	public String getBiztype() {
		return biztype;
	}

	public void setBiztype(String biztype) {
		this.biztype = biztype;
	}

	public String getClosedstatus() {
		return closedstatus;
	}

	public void setClosedstatus(String closedstatus) {
		this.closedstatus = closedstatus;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getApplicantid() {
		return applicantid;
	}

	public void setApplicantid(String applicantid) {
		this.applicantid = applicantid;
	}

	public String getApplicantname() {
		return applicantname;
	}

	public void setApplicantname(String applicantname) {
		this.applicantname = applicantname;
	}

	public String getInsuredid() {
		return insuredid;
	}

	public void setInsuredid(String insuredid) {
		this.insuredid = insuredid;
	}

	public String getInsuredname() {
		return insuredname;
	}

	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}

	public String getCarownerid() {
		return carownerid;
	}

	public void setCarownerid(String carownerid) {
		this.carownerid = carownerid;
	}

	public String getCarownername() {
		return carownername;
	}

	public void setCarownername(String carownername) {
		this.carownername = carownername;
	}

	public String getCarinfoid() {
		return carinfoid;
	}

	public void setCarinfoid(String carinfoid) {
		this.carinfoid = carinfoid;
	}

	public String getStandardfullname() {
		return standardfullname;
	}

	public void setStandardfullname(String standardfullname) {
		this.standardfullname = standardfullname;
	}

	public String getProposalformno() {
		return proposalformno;
	}

	public void setProposalformno(String proposalformno) {
		this.proposalformno = proposalformno;
	}

	public String getPolicyno() {
		return policyno;
	}

	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}

	public Date getInsureddate() {
		return insureddate;
	}

	public void setInsureddate(Date insureddate) {
		this.insureddate = insureddate;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Double getTotalepremium() {
		return totalepremium;
	}

	public void setTotalepremium(Double totalepremium) {
		this.totalepremium = totalepremium;
	}

	public String getAgentnum() {
		return agentnum;
	}

	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getPolicystatus() {
		return policystatus;
	}

	public void setPolicystatus(String policystatus) {
		this.policystatus = policystatus;
	}

	public String getRisktype() {
		return risktype;
	}

	public void setRisktype(String risktype) {
		this.risktype = risktype;
	}

	public String getPaynum() {
		return paynum;
	}

	public void setPaynum(String paynum) {
		this.paynum = paynum;
	}

	public String getCheckcode() {
		return checkcode;
	}

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getDiscountCharge() {
		return discountCharge;
	}

	public void setDiscountCharge(Double discountCharge) {
		this.discountCharge = discountCharge;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	
	public String getShouyirenname() {
		return shouyirenname;
	}

	public void setShouyirenname(String shouyirenname) {
		this.shouyirenname = shouyirenname;
	}
	
	public String getStartdates() {
		return startdates;
	}

	public void setStartdates(String startdates) {
		this.startdates = startdates;
	}

	public String getEnddates() {
		return enddates;
	}

	public void setEnddates(String enddates) {
		this.enddates = enddates;
	}
	
	public String getBeibaorenname() {
		return beibaorenname;
	}

	public void setBeibaorenname(String beibaorenname) {
		this.beibaorenname = beibaorenname;
	}
	
	public String getToubaorenname() {
		return toubaorenname;
	}

	public void setToubaorenname(String toubaorenname) {
		this.toubaorenname = toubaorenname;
	}
	
	public String getToubaoidcardno() {
		return toubaoidcardno;
	}

	public void setToubaoidcardno(String toubaoidcardno) {
		this.toubaoidcardno = toubaoidcardno;
	}

	public String getContactsname() {
		return contactsname;
	}

	public void setContactsname(String contactsname) {
		this.contactsname = contactsname;
	}

	@Override
	public String toString() {
		return "INSBPolicyitem [taskid=" + taskid + ", orderid=" + orderid
				+ ", applicantid=" + applicantid + ", applicantname="
				+ applicantname + ", insuredid=" + insuredid + ", insuredname="
				+ insuredname + ", carownerid=" + carownerid
				+ ", carownername=" + carownername + ", shouyirenname="
				+ shouyirenname + ", beibaorenname=" + beibaorenname
				+ ", toubaorenname=" + toubaorenname + ", carinfoid="
				+ carinfoid + ", standardfullname=" + standardfullname
				+ ", proposalformno=" + proposalformno + ", policyno="
				+ policyno + ", premium=" + premium + ", insureddate="
				+ insureddate + ", startdate=" + startdate + ", startdates="
				+ startdates + ", enddate=" + enddate + ", enddates="
				+ enddates + ", totalepremium=" + totalepremium + ", agentnum="
				+ agentnum + ", agentname=" + agentname + ", team=" + team
				+ ", policystatus=" + policystatus + ", closedstatus="
				+ closedstatus + ", risktype=" + risktype + ", paynum="
				+ paynum + ", checkcode=" + checkcode + ", discountCharge="
				+ discountCharge + ", discountRate=" + discountRate
				+ ", amount=" + amount + ", inscomcode=" + inscomcode
				+ ", toubaoidcardno=" + toubaoidcardno
				+ ", contactsname=" + contactsname
				+ "]";
	}


	
}