package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSHQuoteinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 报价总表id
	 */
	private String quotetotalinfoid;

	/**
	 * 车主姓名
	 */
	private String ownername;

	/**
	 * 车牌号
	 */
	private String platenumber;

	/**
	 * 报价信息
	 */
	private String quoteinfo;

	/**
	 * 报价结果
	 */
	private String quoteresult;

	/**
	 * 报价标准总金额
	 */
	private Double quoteamount;

	/**
	 * 折扣率
	 */
	private String discountrate;

	/**
	 * 保险公司代码
	 */
	private String inscomcode;

	/**
	 * 保险省份代码
	 */
	private String insprovincecode;

	/**
	 * 保险省份名称
	 */
	private String insprovincename;

	/**
	 * 保险城市代码
	 */
	private String inscitycode;

	/**
	 * 保险城市名称
	 */
	private String inscityname;

	/**
	 * 确保方式
	 */
	private String insureway;

	/**
	 * 报价状态
	 */
	private String taskstatus;

	/**
	 * 工作流对应的报价id
	 */
	private String workflowinstanceid;

	/**
	 * 出单网点对应的机构id
	 */
	private String deptcode;

	/**
	 * 协议id
	 */
	private String agreementid;

	/**
	 * 报价折扣总金额
	 */
	private Double quotediscountamount;

	/**
	 * 购买时供应商任务类型选项（01传统02网销03电销）
	 */
	private String buybusitype;

	/**
	 * 精灵或edi，robot-精灵，edi-EDI
	 */
	private String fairyoredi;

	/**
	 * 流程节点，A-报价，B-核保回写，D-承保回写
	 */
	private String nodecode;

	public String getQuotetotalinfoid() {
		return quotetotalinfoid;
	}

	public void setQuotetotalinfoid(String quotetotalinfoid) {
		this.quotetotalinfoid = quotetotalinfoid;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public String getPlatenumber() {
		return platenumber;
	}

	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}

	public String getQuoteinfo() {
		return quoteinfo;
	}

	public void setQuoteinfo(String quoteinfo) {
		this.quoteinfo = quoteinfo;
	}

	public String getQuoteresult() {
		return quoteresult;
	}

	public void setQuoteresult(String quoteresult) {
		this.quoteresult = quoteresult;
	}

	public Double getQuoteamount() {
		return quoteamount;
	}

	public void setQuoteamount(Double quoteamount) {
		this.quoteamount = quoteamount;
	}

	public String getDiscountrate() {
		return discountrate;
	}

	public void setDiscountrate(String discountrate) {
		this.discountrate = discountrate;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getInsprovincecode() {
		return insprovincecode;
	}

	public void setInsprovincecode(String insprovincecode) {
		this.insprovincecode = insprovincecode;
	}

	public String getInsprovincename() {
		return insprovincename;
	}

	public void setInsprovincename(String insprovincename) {
		this.insprovincename = insprovincename;
	}

	public String getInscitycode() {
		return inscitycode;
	}

	public void setInscitycode(String inscitycode) {
		this.inscitycode = inscitycode;
	}

	public String getInscityname() {
		return inscityname;
	}

	public void setInscityname(String inscityname) {
		this.inscityname = inscityname;
	}

	public String getInsureway() {
		return insureway;
	}

	public void setInsureway(String insureway) {
		this.insureway = insureway;
	}

	public String getTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}

	public String getWorkflowinstanceid() {
		return workflowinstanceid;
	}

	public void setWorkflowinstanceid(String workflowinstanceid) {
		this.workflowinstanceid = workflowinstanceid;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public Double getQuotediscountamount() {
		return quotediscountamount;
	}

	public void setQuotediscountamount(Double quotediscountamount) {
		this.quotediscountamount = quotediscountamount;
	}

	public String getBuybusitype() {
		return buybusitype;
	}

	public void setBuybusitype(String buybusitype) {
		this.buybusitype = buybusitype;
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