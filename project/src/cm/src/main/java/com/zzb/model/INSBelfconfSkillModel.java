package com.zzb.model;


public class INSBelfconfSkillModel {
	/**
	 * 精灵id
	 */
	private String elfid;
	/**
	 * 精灵名称
	 */
	private String elfname;

	/**
	 * 存放路径
	 */
	private String elfpath;
	/**
	 * 保险公司id
	 */
	private String proid;
	
	/**
	 * 精灵类型1：半自动2全自动统一3全自动非统一
	 */
	private String elftype;
	
	/**
	 * 能力配置
	 */
	private String capacityconf;
	/**
	 * 技能名称
	 */
	private String skillname;
	/**
	 * 输入项code;
	 */
	private String inputcode;
	/**
	 * 输出项code;
	 */
	private String outputcode;
	
	public String getElfid() {
		return elfid;
	}
	public void setElfid(String elfid) {
		this.elfid = elfid;
	}
	public String getElfname() {
		return elfname;
	}
	public void setElfname(String elfname) {
		this.elfname = elfname;
	}
	public String getElfpath() {
		return elfpath;
	}
	public void setElfpath(String elfpath) {
		this.elfpath = elfpath;
	}
	public String getProid() {
		return proid;
	}
	public void setProid(String proid) {
		this.proid = proid;
	}
	public String getElftype() {
		return elftype;
	}
	public void setElftype(String elftype) {
		this.elftype = elftype;
	}
	public String getCapacityconf() {
		return capacityconf;
	}
	public void setCapacityconf(String capacityconf) {
		this.capacityconf = capacityconf;
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
