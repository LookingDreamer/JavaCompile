package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSHQuotetotalinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

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
	 * 投保地区
	 */
	private String insurancearea;

	/**
	 * 险种代码
	 */
	private String riskcode;

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
	 * 是否续保 0 不是 1是
	 */
	private String isrenewal;

	/**
	 * 是否保存投保书    0无投保书  1保存投保书
	 */
	private String insurancebooks;

	/**
	 * 0 车辆信息，1车型信息，2供应商列表
	 */
	private String webpagekey;

	/**
	 * 平台查询状态 1 true 0 false
	 */
	private String cloudstate;

	/**
	 * 手工选择的投保公司名称
	 */
	private String handersupplier;

	/**
	 * 查询到的上年投保公司名称
	 */
	private String lastyearsupplier;

	/**
	 * 业管id
	 */
	private String userid;

	/**
	 * 购买人id，通过懒掌柜购买
	 */
	private String purchaserid;

	/**
	 * 懒掌柜分享人的id
	 */
	private String lzgshareid;

	/**
	 * 渠道回调地址
	 */
	private String callbackurl;

	/**
	 * 购买渠道ID
	 */
	private String purchaserchannel;

	/**
	 * 来源
	 */
	private String sourcefrom;

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

	public String getInsurancearea() {
		return insurancearea;
	}

	public void setInsurancearea(String insurancearea) {
		this.insurancearea = insurancearea;
	}

	public String getRiskcode() {
		return riskcode;
	}

	public void setRiskcode(String riskcode) {
		this.riskcode = riskcode;
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

	public String getIsrenewal() {
		return isrenewal;
	}

	public void setIsrenewal(String isrenewal) {
		this.isrenewal = isrenewal;
	}

	public String getInsurancebooks() {
		return insurancebooks;
	}

	public void setInsurancebooks(String insurancebooks) {
		this.insurancebooks = insurancebooks;
	}

	public String getWebpagekey() {
		return webpagekey;
	}

	public void setWebpagekey(String webpagekey) {
		this.webpagekey = webpagekey;
	}

	public String getCloudstate() {
		return cloudstate;
	}

	public void setCloudstate(String cloudstate) {
		this.cloudstate = cloudstate;
	}

	public String getHandersupplier() {
		return handersupplier;
	}

	public void setHandersupplier(String handersupplier) {
		this.handersupplier = handersupplier;
	}

	public String getLastyearsupplier() {
		return lastyearsupplier;
	}

	public void setLastyearsupplier(String lastyearsupplier) {
		this.lastyearsupplier = lastyearsupplier;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPurchaserid() {
		return purchaserid;
	}

	public void setPurchaserid(String purchaserid) {
		this.purchaserid = purchaserid;
	}

	public String getLzgshareid() {
		return lzgshareid;
	}

	public void setLzgshareid(String lzgshareid) {
		this.lzgshareid = lzgshareid;
	}

	public String getCallbackurl() {
		return callbackurl;
	}

	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}

	public String getPurchaserchannel() {
		return purchaserchannel;
	}

	public void setPurchaserchannel(String purchaserchannel) {
		this.purchaserchannel = purchaserchannel;
	}

	public String getSourcefrom() {
		return sourcefrom;
	}

	public void setSourcefrom(String sourcefrom) {
		this.sourcefrom = sourcefrom;
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