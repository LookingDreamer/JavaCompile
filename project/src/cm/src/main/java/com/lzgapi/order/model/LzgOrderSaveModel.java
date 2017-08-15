package com.lzgapi.order.model;

import java.util.ArrayList;
import java.util.List;




public class LzgOrderSaveModel {
	/**
	 * 任务号ID
	 */
	private String id;
	/**
	 * 订单编号
	 */
	private String orderno;
	/**
	 * 平台
	 */
	private Integer plantform;
	/**
	 * 数量
	 */
	private Integer quantity;
	/**
	 * 单价
	 */
	private Double price;
	/**
	 * 总价
	 */
	private Double totalprice;
	/**
	 * 订单状态：0-未完成；1-已完成；2-已删除;9-已撤销
	 */
	private String status;
	
	/**
	 * 保单信息
	 */
	private List<LzgPolicyModel>  policylist = new ArrayList<LzgPolicyModel>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public Integer getPlantform() {
		return plantform;
	}

	public void setPlantform(Integer plantform) {
		this.plantform = plantform;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
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
