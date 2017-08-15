package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBQuotetotalinfo extends BaseEntity implements Identifiable {
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
     * 投保书标记页面位置 0 车辆信息，1车型信息，2供应商列表
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
     * 通过懒掌柜购买的人的id
     */
    private String purchaserid;
    /**
     * 懒掌柜分享人的id
     */
    private String lzgshareid;
    /**
     * 购买的来源
     */
    private String sourcefrom;
    /**
     * 回调地址
     */
    private String callbackurl;
    /**
     * 购买的渠道
     */
    private String purchaserchannel;
    private String purchaserchannelName;//购买渠道名称
	/**
     * 业务来源 IOS版   Android版   网页版  微信版     渠道对接    其他
     */
    private String datasourcesfrom;
    
    public String getDatasourcesfrom() {
		return datasourcesfrom;
	}
	public void setDatasourcesfrom(String datasourcesfrom) {
		this.datasourcesfrom = datasourcesfrom;
	}
    public String getLzgshareid() {
		return lzgshareid;
	}

	public void setLzgshareid(String lzgshareid) {
		this.lzgshareid = lzgshareid;
	}

	public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getWebpagekey() {
        return webpagekey;
    }

    public void setWebpagekey(String webpagekey) {
        this.webpagekey = webpagekey;
    }

    public String getInsurancebooks() {
        return insurancebooks;
    }

    public void setInsurancebooks(String insurancebooks) {
        this.insurancebooks = insurancebooks;
    }

    public String getIsrenewal() {
        return isrenewal;
    }

    public void setIsrenewal(String isrenewal) {
        this.isrenewal = isrenewal;
    }

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

    public String getPurchaserid() {
        return purchaserid;
    }

    public void setPurchaserid(String purchaserid) {
        this.purchaserid = purchaserid;
    }

    public String getSourceFrom() {
        return sourcefrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourcefrom = sourceFrom;
    }

    public String getCallBackUrl() {
        return callbackurl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callbackurl = callBackUrl;
    }

    public String getPurchaserChannel() {
        return purchaserchannel;
    }

    public void setPurchaserChannel(String purchaserChannel) {
        this.purchaserchannel = purchaserChannel;
    }
	public String getPurchaserchannelName() {
		return purchaserchannelName;
	}
	public void setPurchaserchannelName(String purchaserchannelName) {
		this.purchaserchannelName = purchaserchannelName;
	}
    
}