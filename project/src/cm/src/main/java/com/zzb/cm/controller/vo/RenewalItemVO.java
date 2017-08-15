package com.zzb.cm.controller.vo;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class RenewalItemVO extends BaseEntity implements Identifiable {
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
     * 数据项值
     */
    private String itemvalue;

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
    private int itemorder;

    /**
     * 是否必填
     */
    private int isrequired;

    /**
     * 协议id
     */
    private String agreementid;

    /**
     * 任务id
     */
    private String taskid;

    /**
     * 保险公司代码
     */
    private String inscomcode;

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

    public int getItemorder() {
        return itemorder;
    }

    public void setItemorder(int itemorder) {
        this.itemorder = itemorder;
    }

    public int isIsrequired() {
        return isrequired;
    }

    public void setIsrequired(int isrequired) {
        this.isrequired = isrequired;
    }

    public String getAgreementid() {
        return agreementid;
    }

    public void setAgreementid(String agreementid) {
        this.agreementid = agreementid;
    }

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
}