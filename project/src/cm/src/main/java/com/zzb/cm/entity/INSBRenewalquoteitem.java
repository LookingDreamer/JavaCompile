package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRenewalquoteitem extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private String taskid;
    /**
     * 保险公司代码
     */
    private String inscomcode;
	/**
	 * 数据项编码
	 */
	private String itemcode;

	/**
	 * 数据项名称
	 */
	private String itemname;

	/**
	 * 数据项值
	 */
	private String itemvalue;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getInscomcode() {
        return inscomcode;
    }

    public void setInscomcode(String inscomcode) {
        this.inscomcode = inscomcode;
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

    public String getItemvalue() {
        return itemvalue;
    }

    public void setItemvalue(String itemvalue) {
        this.itemvalue = itemvalue;
    }

    @Override
    public String toString() {
        return "INSBRenewalItem [itemcode=" + itemcode + ", itemname="
                + itemname + ", itemvalue=" + itemvalue
                + ", taskid=" + taskid + ", inscomcode=" + inscomcode + "]";
    }
}