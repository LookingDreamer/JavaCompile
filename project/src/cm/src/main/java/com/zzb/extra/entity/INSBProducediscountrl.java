package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBProducediscountrl extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商编号
	 */
	private String companyno;

	/**
	 * 优惠产品ID
	 */
	private String discountid;

	public String getCompanyno() {
		return companyno;
	}

	public void setCompanyno(String companyno) {
		this.companyno = companyno;
	}

	public String getDiscountid() {
		return discountid;
	}

	public void setDiscountid(String discountid) {
		this.discountid = discountid;
	}

}