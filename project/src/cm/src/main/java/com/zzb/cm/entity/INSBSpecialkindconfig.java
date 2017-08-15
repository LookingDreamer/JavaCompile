package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBSpecialkindconfig extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主实例id
	 */
	private String taskid;

	/**
	 * 字典表value  04 key-value 05 value
	 */
	private String typecode;

	/**
	 * 供应商编码
	 */
	private String inscomcode;

	/**
	 * 险别编码
	 */
	private String kindcode;

	/**
	 * key名称
	 */
	private String codekey;

	/**
	 * 值
	 */
	private String codevalue;

	/**
	 * 备用1
	 */
	private String prop1;

	/**
	 * 备用2
	 */
	private String prop2;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTypecode() {
		return typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getKindcode() {
		return kindcode;
	}

	public void setKindcode(String kindcode) {
		this.kindcode = kindcode;
	}

	public String getCodekey() {
		return codekey;
	}

	public void setCodekey(String codekey) {
		this.codekey = codekey;
	}

	public String getCodevalue() {
		return codevalue;
	}

	public void setCodevalue(String codevalue) {
		this.codevalue = codevalue;
	}

	public String getProp1() {
		return prop1;
	}

	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}

	public String getProp2() {
		return prop2;
	}

	public void setProp2(String prop2) {
		this.prop2 = prop2;
	}

}