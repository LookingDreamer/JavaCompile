package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRenewalconfigitem extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

    /**
     * 协议id
     */
    private String agreementid;
	/**
	 * 数据项编码
	 */
	private String itemcode;

	/**
	 * 数据项名称
	 */
	private String itemname;

	/**
	 * 数据项排序
	 */
	private Integer itemorder;

	/**
	 * 是否必填
	 */
	private Integer isrequired;

    public String getAgreementid() {
        return agreementid;
    }

    public void setAgreementid(String agreementid) {
        this.agreementid = agreementid;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Integer getItemorder() {
        return itemorder;
    }

    public void setItemorder(Integer itemorder) {
        this.itemorder = itemorder;
    }

    public Integer getIsrequired() {
        return isrequired;
    }

    public void setIsrequired(Integer isrequired) {
        this.isrequired = isrequired;
    }

    @Override
    public String toString() {
        return "INSBRenewalItem [itemcode=" + itemcode + ", itemname="
                + itemname + ", itemorder=" + itemorder
                + ", isrequired=" + isrequired + ", agreementid=" + agreementid + "]";
    }
}