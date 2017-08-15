package com.zzb.mobile.model;

import javax.validation.constraints.NotNull;

public class PayChannelParamVo {
	//(insbOrderpayment.getPaychannelid(), insbOrderpayment.getOperator(), insbOrderpayment.getTaskid()
	private String prvid;
	private String deptcode;
    @NotNull(message = "需要终端类型参数clienttype, 值为web,android,ios,wechat")
	private String clienttype;
	private String paychannelid;
	private String operator;
	private String taskid;
    @NotNull(message = "需要参数subInstanceId")
    private String subInstanceId;
	private String inscomcode;
	private String channelId;
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getPaychannelid() {
		return paychannelid;
	}
	public void setPaychannelid(String paychannelid) {
		this.paychannelid = paychannelid;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public PayChannelParamVo(){}
	public String getPrvid() {
		return prvid;
	}
	public void setPrvid(String prvid) {
		this.prvid = prvid;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode =deptcode;
	}

	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

    public String getSubInstanceId() {
        return subInstanceId;
    }

    public void setSubInstanceId(String subInstanceId) {
        this.subInstanceId = subInstanceId;
    }

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
