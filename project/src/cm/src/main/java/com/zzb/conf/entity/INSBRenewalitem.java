package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRenewalitem extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/**
	 * 数据项编码
	 */
	private String itemcode;

	/**
	 * 数据项名称
	 */
	private String itemname;

	/**
	 * 数据项输入类型（文件、下拉列表、日期控件等）
	 */
	private String iteminputtype;

	/**
	 * 数据项为下拉列表时可选项列表
	 */
	private String itemvaluelist;

    /**
     * 数据项排序
     */
    private Integer itemorder;

	/**
	 * 是否必填
	 */
	private Integer isrequired;

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

    public String getIteminputtype() {
        return iteminputtype;
    }

    public void setIteminputtype(String iteminputtype) {
        this.iteminputtype = iteminputtype;
    }

    public String getItemvaluelist() {
        return itemvaluelist;
    }

    public void setItemvaluelist(String itemvaluelist) {
        this.itemvaluelist = itemvaluelist;
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
                + ", iteminputtype=" + iteminputtype + ", itemvaluelist="
                + itemvaluelist + ", isrequired=" + isrequired + "]";
    }
}