package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBRealtimetask extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务类型编码
	 */
	private String tasktype;

	/**
	 * 任务类型名称
	 */
	private String tasktypename;

	/**
	 * 车牌号
	 */
	private String carlicenseno;

	/**
	 * 被保人id
	 */
	private String insuredid;

	/**
	 * 被保人姓名
	 */
	private String insuredname;

	/**
	 * 被保人证件号码
	 */
	private String insuredcardno;

	/**
	 * 投保人id
	 */
	private String applicantid;

	/**
	 * 投保人姓名
	 */
	private String applicantname;

	/**
	 * 投保人证件号码
	 */
	private String applicantcardno;

	/**
	 * 代理人工号
	 */
	private String agentNum;

	/**
	 * 代理人姓名
	 */
	private String agentName;

	/**
	 * 出单网点comcode
	 */
	private String deptcode;

	/**
	 * 出单网点名称
	 */
	private String deptname;

	/**
	 * 出单网点innercode
	 */
	private String deptinnercode;

	/**
	 * 保险公司编码
	 */
	private String inscomcode;

	/**
	 * 保险公司名称
	 */
	private String inscomname;

	/**
	 * 主任务号
	 */
	private String maininstanceid;

	/**
	 * 子任务号
	 */
	private String subinstanceid;

	/**
	 * 处理人
	 */
	private String operatorname;

	/**
	 * 任务激活时间
	 */
	private Date taskcreatetime;

	/**
	 * 业务来源
	 */
	private String datasourcesfrom;

	/**
	 * 渠道编码
	 */
	private String channelcode;

	/**
	 * 渠道名称
	 */
	private String channelname;

	private String operatorcode;

	private String operatorstate;

	public String getOperatorstate() {
		return operatorstate;
	}

	public void setOperatorstate(String operatorstate) {
		this.operatorstate = operatorstate;
	}

	public String getOperatorcode() {
		return operatorcode;
	}

	public void setOperatorcode(String operatorcode) {
		this.operatorcode = operatorcode;
	}

	public String getTasktype() {
		return tasktype;
	}

	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}

	public String getTasktypename() {
		return tasktypename;
	}

	public void setTasktypename(String tasktypename) {
		this.tasktypename = tasktypename;
	}

	public String getCarlicenseno() {
		return carlicenseno;
	}

	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
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

	public String getInsuredcardno() {
		return insuredcardno;
	}

	public void setInsuredcardno(String insuredcardno) {
		this.insuredcardno = insuredcardno;
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

	public String getApplicantcardno() {
		return applicantcardno;
	}

	public void setApplicantcardno(String applicantcardno) {
		this.applicantcardno = applicantcardno;
	}

	public String getAgentNum() {
		return agentNum;
	}

	public void setAgentNum(String agentNum) {
		this.agentNum = agentNum;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getDeptinnercode() {
		return deptinnercode;
	}

	public void setDeptinnercode(String deptinnercode) {
		this.deptinnercode = deptinnercode;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getInscomname() {
		return inscomname;
	}

	public void setInscomname(String inscomname) {
		this.inscomname = inscomname;
	}

	public String getMaininstanceid() {
		return maininstanceid;
	}

	public void setMaininstanceid(String maininstanceid) {
		this.maininstanceid = maininstanceid;
	}

	public String getSubinstanceid() {
		return subinstanceid;
	}

	public void setSubinstanceid(String subinstanceid) {
		this.subinstanceid = subinstanceid;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public Date getTaskcreatetime() {
		return taskcreatetime;
	}

	public void setTaskcreatetime(Date taskcreatetime) {
		this.taskcreatetime = taskcreatetime;
	}

	public String getDatasourcesfrom() {
		return datasourcesfrom;
	}

	public void setDatasourcesfrom(String datasourcesfrom) {
		this.datasourcesfrom = datasourcesfrom;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

}