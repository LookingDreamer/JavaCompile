package com.zzb.extra.model;

import com.zzb.model.OrderQueryModel;

/**
 * Created by Administrator on 2016/11/30.
 */
public class MiniOrderQueryModel extends OrderQueryModel {
    /**
     * mini用户ID
     */
    private String agentid;
    
    private String name;
    private String channelId;

    private String taskId;

    private String prvId;

    private String channelUserId;

    private String createTimeStart;

    private String createTimeEnd;

    private String carLicenseNo;

    private String insureName;

    private String carLicenseNoOrInsureName;

    private String refundOrder;

    private String willInvalidOrder;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public String getPrvId() {
        return prvId;
    }

    public void setPrvId(String prvId) {
        this.prvId = prvId;
    }


    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getCarLicenseNo() {
        return carLicenseNo;
    }

    public void setCarLicenseNo(String carLicenseNo) {
        this.carLicenseNo = carLicenseNo;
    }

    public String getInsureName() {
        return insureName;
    }

    public void setInsureName(String insureName) {
        this.insureName = insureName;
    }

    public String getCarLicenseNoOrInsureName() {
        return carLicenseNoOrInsureName;
    }

    public void setCarLicenseNoOrInsureName(String carLicenseNoOrInsureName) {
        this.carLicenseNoOrInsureName = carLicenseNoOrInsureName;
    }

    public String getRefundOrder() {
        return refundOrder;
    }

    public void setRefundOrder(String refundOrder) {
        this.refundOrder = refundOrder;
    }

    public String getWillInvalidOrder() {
        return willInvalidOrder;
    }

    public void setWillInvalidOrder(String willInvalidOrder) {
        this.willInvalidOrder = willInvalidOrder;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    private String taskState;

    private String pageNum;

    private String pageSize;
    
    private String phoneMini;

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneMini() {
		return phoneMini;
	}

	public void setPhoneMini(String phoneMini) {
		this.phoneMini = phoneMini;
	}
	
	
	

	
}
