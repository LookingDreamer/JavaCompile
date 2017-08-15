package com.zzb.cm.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBSupplement extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L; 

	/**
	 * 补充项key
	 */
	private String keyid;

	/**
	 * 供应商id
	 */
	private String providerid;
	/**
	 * 供应商ids
	 */
	private List<String> providerids;
	
	public List<String> getProviderids() {
		return providerids;
	}

	public void setProviderids(List<String> providerids) {
		this.providerids = providerids;
	}
	/**
	 * 规则id
	 */
	private String ruleid;

	/**
	 * 补充项中文名
	 */
	private String metadataName;

	/**
	 * 补充项值
	 */
	private String metadataValue;
	/**
	 * 补充项值 Key
	 */
	private String metadataValueKey;

	public String getMetadataValueKey() {
		return metadataValueKey;
	}

	public void setMetadataValueKey(String metadataValueKey) {
		this.metadataValueKey = metadataValueKey;
	}
	// 如果是选择列表，选择列表转换后的Map
	private List<Map<String, String>> metadataValueMapList;
	/**
	 * 车险任务id
	 */
	private String taskid;

	/**
	 * 
	 */
	private Date optime;

	/**
	 * 
	 */
	private String opuser;
	private int ordernum=999;
	public int getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}
	public String getKeyid() {
		return keyid;
	}

	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public String getRuleid() {
		return ruleid;
	}

	public void setRuleid(String ruleid) {
		this.ruleid = ruleid;
	}

	public String getMetadataName() {
		return metadataName;
	}

	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}

	public String getMetadataValue() {
		return metadataValue;
	}

	public void setMetadataValue(String metadataValue) {
		this.metadataValue = metadataValue;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Date getOptime() {
		return optime;
	}

	public void setOptime(Date optime) {
		this.optime = optime;
	}

	public String getOpuser() {
		return opuser;
	}

	public void setOpuser(String opuser) {
		this.opuser = opuser;
	}

	public List<Map<String, String>> getMetadataValueMapList() {
		return metadataValueMapList;
	}

	public void setMetadataValueMapList(List<Map<String, String>> metadataValueMapList) {
		this.metadataValueMapList = metadataValueMapList;
	}

}