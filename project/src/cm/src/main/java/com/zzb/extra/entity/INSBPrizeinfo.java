package com.zzb.extra.entity;


import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBPrizeinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 奖品名称
	 */
	private String name;

	/**
	 * 奖品类型
	 */
	private Integer type;

	/**
	 * 数额
	 */
	private Integer counts;

	/**
	 * 生效时间
	 */
	private String  effectivetime;

	/**
	 * 失效时间
	 */
	private String invalidtime;

	/**
	 * 生效天数
	 */
	private Integer effectiveDates;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public String getEffectivetime() {
		return effectivetime;
	}

	public void setEffectivetime(String effectivetime) {
		this.effectivetime = effectivetime;
	}

	public String getInvalidtime() {
		return invalidtime;
	}

	public void setInvalidtime(String invalidtime) {
		this.invalidtime = invalidtime;
	}

	public Integer getEffectiveDates() {
		return effectiveDates;
	}

	public void setEffectiveDates(Integer effectiveDates) {
		this.effectiveDates = effectiveDates;
	}

}