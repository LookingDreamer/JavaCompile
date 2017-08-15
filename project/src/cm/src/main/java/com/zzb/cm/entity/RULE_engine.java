package com.zzb.cm.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.Identifiable;

public class RULE_engine  implements Identifiable {
	private static final long serialVersionUID = 1L;
	private String id;
	/**
	 * 
	 */
	private Integer  rule_engine_id;

	/**
	 * 
	 */
	private Integer  rule_base_type;

	/**
	 * 
	 */
	private Integer  city_id;

	/**
	 * 
	 */
	private Integer  company_id;

	/**
	 * 
	 */
	private String rule_base_name;

	/**
	 * 
	 */
	private String  rule_base_content;

	/**
	 * 
	 */
	private String rule_base_postil;

	/**
	 * 
	 */
	private Integer status;

	/**
	 * 
	 */
	private Date last_updated;

	/**
	 * 
	 */
	private String  metadata_item;

	public Integer getRule_engine_id() {
		return rule_engine_id;
	}

	public void setRule_engine_id(Integer rule_engine_id) {
		this.rule_engine_id = rule_engine_id;
	}

	public Integer getRule_base_type() {
		return rule_base_type;
	}

	public void setRule_base_type(Integer rule_base_type) {
		this.rule_base_type = rule_base_type;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Integer getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public String getRule_base_name() {
		return rule_base_name;
	}

	public void setRule_base_name(String rule_base_name) {
		this.rule_base_name = rule_base_name;
	}

	public String getRule_base_content() {
		return rule_base_content;
	}

	public void setRule_base_content(String rule_base_content) {
		this.rule_base_content = rule_base_content;
	}

	public String getRule_base_postil() {
		return rule_base_postil;
	}

	public void setRule_base_postil(String rule_base_postil) {
		this.rule_base_postil = rule_base_postil;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(Date last_updated) {
		this.last_updated = last_updated;
	}

	public String getMetadata_item() {
		return metadata_item;
	}

	public void setMetadata_item(String metadata_item) {
		this.metadata_item = metadata_item;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "RULE_engine [id=" + id + ", rule_engine_id=" + rule_engine_id
				+ ", rule_base_type=" + rule_base_type + ", city_id=" + city_id
				+ ", company_id=" + company_id + ", rule_base_name="
				+ rule_base_name + ", rule_base_content=" + rule_base_content
				+ ", rule_base_postil=" + rule_base_postil + ", status="
				+ status + ", last_updated=" + last_updated
				+ ", metadata_item=" + metadata_item + "]";
	}
	
}