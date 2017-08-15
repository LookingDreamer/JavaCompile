package com.zzb.cm.controller.vo;

/*
 * MediumPaymentController 接口中传递的参数，
 * 
 */
public class MediumPaymentVo {
	private String userid;
	/*
	 * 用户id
	 */
	
	private String taskid;
	/*
	 * 任务id
	 */
	
	private String processinstanceid;
	private String inscomcode;

    /*
     *交强险单号
     */
    private String ciPolicyNo;
    /*
     *商业险单号
     */
    private String biPolicyNo;
	/*
	 * 实例流程id
	 */
	
	private Integer claim;
	/*
	 *  保险公司列表、下一个节点是否自动认领(0为不需要，1为需要)
	 */
	
	private String isRebackWriting;
	/*
	 * 是否同时完成回写
	 */
	
	private String tracenumber;
	/*
	 * 快递的配送编号
	 */
	
	private String codevalue;
	/*
	 * 物流公司的编号
	 */
	
	private String deltype;
	/**
	 * 配送方式
	 */
	
	private String issecond;
	/*
	 * 支付
	 */
	
	/*
	 * 重新核保标记0，认领前；1，认领后
	 */
	private String underWritingFlag;
	
	public String getUnderWritingFlag() {
		return underWritingFlag;
	}

	public void setUnderWritingFlag(String underWritingFlag) {
		this.underWritingFlag = underWritingFlag;
	}

	public String getIssecond() {
		return issecond;
	}

	public void setIssecond(String issecond) {
		this.issecond = issecond;
	}

	MediumPaymentVo(){}
	
	public String getDeltype() {
		return deltype;
	}

	public void setDeltype(String deltype) {
		this.deltype = deltype;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getTracenumber() {
		return tracenumber;
	}
	public void setTracenumber(String tracenumber) {
		this.tracenumber = tracenumber;
	}
	public String getCodevalue() {
		return codevalue;
	}
	public void setCodevalue(String codevalue) {
		this.codevalue = codevalue;
	}
	public String getIsRebackWriting() {
		return isRebackWriting;
	}
	public void setIsRebackWriting(String isRebackWriting) {
		this.isRebackWriting = isRebackWriting;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public Integer getClaim() {
		return claim;
	}
	public void setClaim(Integer claim) {
		this.claim = claim;
	}
    public String getCiPolicyNo() {
        return ciPolicyNo;
    }
    public void setCiPolicyNo(String ciPolicyNo) {
        this.ciPolicyNo = ciPolicyNo;
    }
    public String getBiPolicyNo() {
        return biPolicyNo;
    }
    public void setBiPolicyNo(String biPolicyNo) {
        this.biPolicyNo = biPolicyNo;
    }
}
