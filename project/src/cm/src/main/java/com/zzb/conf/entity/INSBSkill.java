package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBSkill extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 精灵id
	 */
	private String elfid;

	/**
	 * 技能名称
	 */
	private String skillname;

	/**
	 * 输入项code，对应字典表codevalue :inputItem
	 */
	private String inputcode;

	/**
	 * 输出项code，对应字典表codevalue :outputItem
	 */
	private String outputcode;

	public String getElfid() {
		return elfid;
	}

	public void setElfid(String elfid) {
		this.elfid = elfid;
	}

	public String getSkillname() {
		return skillname;
	}

	public void setSkillname(String skillname) {
		this.skillname = skillname;
	}

	public String getInputcode() {
		return inputcode;
	}

	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}

	public String getOutputcode() {
		return outputcode;
	}

	public void setOutputcode(String outputcode) {
		this.outputcode = outputcode;
	}

}