package com.lzgapi.order.model;

import java.util.ArrayList;
import java.util.List;

public class LzgOrderUpdateModel {

	/**
	 * 订单编号
	 */
	private String orderno;

	/**
	 * 平台
	 */
	private Integer platform;

	/**
	 * 订单状态：0-未完成；1-已完成；2-已删除;9-已撤销
	 */
	private String status;

	/**
	 * 保单信息
	 */
	private List<LzgPolicyModel> policylist = new ArrayList<LzgPolicyModel>();

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<LzgPolicyModel> getPolicylist() {
		return policylist;
	}

	public void setPolicylist(List<LzgPolicyModel> policylist) {
		this.policylist = policylist;
	}
	
	
}
