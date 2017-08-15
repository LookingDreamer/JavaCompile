package com.zzb.mobile.model;


public class WorkFlowRestartConfModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 供应商id列表
	 */
	private String pid;
	/**
	 * 校验通过 true  没通过false
	 */
	private boolean flag;
	/**
     * 当前操作标志，0 选择投保   1 核保退回修改信息 2 报价退回
     */
    private String flowflag;
	
	public String getFlowflag() {
		return flowflag;
	}

	public void setFlowflag(String flowflag) {
		this.flowflag = flowflag;
	}

	public String getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
