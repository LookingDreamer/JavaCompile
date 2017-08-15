package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBPlatcarprice extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 
	 */
	private String companyid;

	/**
	 * 品牌名称
	 */
	private String brandname;

	/**
	 * 
	 */
	private Double newcarprice;

	/**
	 * 
	 */
	private Double newcartaxprice;

	/**
	 * 类比价格
	 */
	private Double analogyprice;

	/**
	 * 类比税额
	 */
	private Double analogytaxprice;

	/**
	 * 
	 */
	private String listedyear;

	/**
	 * 承载人数
	 */
	private Integer seat;

	/**
	 * 核定载重量
	 */
	private Integer unwrtweight;

	/**
	 * 整备质量
	 */
	private Integer fullweight;

	/**
	 * 发动机排量
	 */
	private Double displacement;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public Double getNewcarprice() {
		return newcarprice;
	}

	public void setNewcarprice(Double newcarprice) {
		this.newcarprice = newcarprice;
	}

	public Double getNewcartaxprice() {
		return newcartaxprice;
	}

	public void setNewcartaxprice(Double newcartaxprice) {
		this.newcartaxprice = newcartaxprice;
	}

	public Double getAnalogyprice() {
		return analogyprice;
	}

	public void setAnalogyprice(Double analogyprice) {
		this.analogyprice = analogyprice;
	}

	public Double getAnalogytaxprice() {
		return analogytaxprice;
	}

	public void setAnalogytaxprice(Double analogytaxprice) {
		this.analogytaxprice = analogytaxprice;
	}

	public String getListedyear() {
		return listedyear;
	}

	public void setListedyear(String listedyear) {
		this.listedyear = listedyear;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public Integer getUnwrtweight() {
		return unwrtweight;
	}

	public void setUnwrtweight(Integer unwrtweight) {
		this.unwrtweight = unwrtweight;
	}

	public Integer getFullweight() {
		return fullweight;
	}

	public void setFullweight(Integer fullweight) {
		this.fullweight = fullweight;
	}

	public Double getDisplacement() {
		return displacement;
	}

	public void setDisplacement(Double displacement) {
		this.displacement = displacement;
	}

}