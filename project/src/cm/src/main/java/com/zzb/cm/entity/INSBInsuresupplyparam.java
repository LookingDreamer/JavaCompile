package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.cninsure.core.utils.StringUtil;

import java.util.List;
import java.util.Map;


public class INSBInsuresupplyparam extends BaseEntity implements Identifiable {
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

	/**
	 * 数据项输入类型（文件、下拉列表、日期控件等）
	 */
	private String iteminputtype;

	/**
	 * 数据项为下拉列表时可选项列表
	 */
	private String itemoptionallist;

	private Map itemoptions;

	/**
	 * 数据项排序
	 */
	private Integer itemorder;

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

	public String getIteminputtype() {
		return iteminputtype;
	}

	public void setIteminputtype(String iteminputtype) {
		this.iteminputtype = iteminputtype;
	}

	public String getItemoptionallist() {
		return itemoptionallist;
	}

	public void setItemoptionallist(String itemoptionallist) {
		this.itemoptionallist = itemoptionallist;
	}

	public Integer getItemorder() {
		return itemorder;
	}

	public void setItemorder(Integer itemorder) {
		this.itemorder = itemorder;
	}

	public Map getItemoptions() {
		return itemoptions;
	}

	public void setItemoptions(Map itemoptions) {
		this.itemoptions = itemoptions;
	}

	public int hashCode() {
		return itemcode == null ? -1 : itemcode.hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof INSBInsuresupplyparam)) return false;

		INSBInsuresupplyparam that = (INSBInsuresupplyparam) obj;
		if (StringUtil.isEmpty(itemcode) && StringUtil.isEmpty(that.getItemcode())) return true;

		return StringUtil.isNotEmpty(itemcode) && itemcode.equalsIgnoreCase(that.getItemcode());
	}
}