package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSHPolicyitem extends BaseEntity implements Identifiable {
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

	/**
	 * 终止日期
	 */
	private Date enddate;

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
	 * 关闭状态；0 未关闭 1 已关闭
	 */
	private String closedstatus;

	/**
	 * 保单状态
	 */
	private String policystatus;

	/**
	 * 险种类型
	 */
	private String risktype;

	/**
	 * 
	 */
	private String paynum;

	/**
	 * 
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
	 * 保险公司代码
	 */
	private String inscomcode;

	/**
	 * 精灵或edi，robot-精灵，edi-EDI
	 */
	private String fairyoredi;

	/**
	 * 流程节点，A-报价，B-核保回写，D-承保回写
	 */
	private String nodecode;

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

	public String getClosedstatus() {
		return closedstatus;
	}

	public void setClosedstatus(String closedstatus) {
		this.closedstatus = closedstatus;
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

	public String getFairyoredi() {
		return fairyoredi;
	}

	public void setFairyoredi(String fairyoredi) {
		this.fairyoredi = fairyoredi;
	}

	public String getNodecode() {
		return nodecode;
	}

	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}

}