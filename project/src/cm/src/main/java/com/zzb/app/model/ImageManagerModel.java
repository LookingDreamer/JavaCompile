package com.zzb.app.model;

/**
 * @author qiu
 *
 */
public class ImageManagerModel {
	
	/**
	 * 报价信息表id（任务id）
	 */
	public String taskid;
	/**
	 * 车主
	 */
	public String carowner;
	
	/**
	 * 被保人
	 */
	public String recognizee;
	
	/**
	 * 车牌号
	 */
	public String  platenum;
	
	/**
	 * 车架号
	 */
	public String  vinno;
	
	/**
	 * 代理人名称
	 */
	public String  agentname;
	
	/**
	 * 影像类型
	 */
	public String  key;
	
	/**
	 * 影像地址
	 */
	public String url;
	
	/**
	 * 是否修改
	 */
	public String  action;
	
	public String getCarowner() {
		return carowner;
	}

	public void setCarowner(String carowner) {
		this.carowner = carowner;
	}

	public String getRecognizee() {
		return recognizee;
	}

	public void setRecognizee(String recognizee) {
		this.recognizee = recognizee;
	}

	public String getPlatenum() {
		return platenum;
	}

	public void setPlatenum(String platenum) {
		this.platenum = platenum;
	}

	public String getVinno() {
		return vinno;
	}

	public void setVinno(String vinno) {
		this.vinno = vinno;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
