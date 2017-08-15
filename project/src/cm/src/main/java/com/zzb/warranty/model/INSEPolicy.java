package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zzb.conf.entity.INSBAgent;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class INSEPolicy extends BaseModel implements Identifiable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单表id
     */
    private String orderid;

    private String policystatus;

    private String biztype;

    private String closedstatus;

    private INSEPerson applicant;

    /**
     * 投保人id
     */
    private String applicantid;

    /**
     * 投保人姓名
     */
    private String applicantname;

    private INSEPerson insured;

    /**
     * 被保人id
     */
    private String insuredid;

    /**
     * 被保人姓名
     */
    private String insuredname;

    private INSEPerson legalrightclaim;

    /**
     * 权益索取人
     */
    private String legalrightclaimid;

    /**
     * 权益索取人名字
     */
    private String legalrightclaimname;

    private INSEPerson carowner;

    /**
     * 车主id
     */
    private String carownerid;

    /**
     * 车主姓名
     */
    private String carownername;

    private INSECar carinfo;

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

    private INSBAgent agent;

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

    private Integer extendwarrantytype;


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

    public String getPolicystatus() {
        return policystatus;
    }

    public void setPolicystatus(String policystatus) {
        this.policystatus = policystatus;
    }

    public String getClosedstatus() {
        return closedstatus;
    }

    public void setClosedstatus(String closedstatus) {
        this.closedstatus = closedstatus;
    }

    public String getBiztype() {
        return biztype;
    }

    public void setBiztype(String biztype) {
        this.biztype = biztype;
    }

    public String getLegalrightclaimid() {
        return legalrightclaimid;
    }

    public void setLegalrightclaimid(String legalrightclaimid) {
        this.legalrightclaimid = legalrightclaimid;
    }

    public String getLegalrightclaimname() {
        return legalrightclaimname;
    }

    public void setLegalrightclaimname(String legalrightclaimname) {
        this.legalrightclaimname = legalrightclaimname;
    }

    public Integer getExtendwarrantytype() {
        return extendwarrantytype;
    }

    public void setExtendwarrantytype(Integer extendwarrantytype) {
        this.extendwarrantytype = extendwarrantytype;
    }

    public INSEPerson getApplicant() {
        return applicant;
    }

    public void setApplicant(INSEPerson applicant) {
        this.applicant = applicant;
    }

    public INSEPerson getInsured() {
        return insured;
    }

    public void setInsured(INSEPerson insured) {
        this.insured = insured;
    }

    public INSEPerson getLegalrightclaim() {
        return legalrightclaim;
    }

    public void setLegalrightclaim(INSEPerson legalrightclaim) {
        this.legalrightclaim = legalrightclaim;
    }

    public INSEPerson getCarowner() {
        return carowner;
    }

    public void setCarowner(INSEPerson carowner) {
        this.carowner = carowner;
    }

    public INSECar getCarinfo() {
        return carinfo;
    }

    public void setCarinfo(INSECar carinfo) {
        this.carinfo = carinfo;
    }

    public INSBAgent getAgent() {
        return agent;
    }

    public void setAgent(INSBAgent agent) {
        this.agent = agent;
    }
}