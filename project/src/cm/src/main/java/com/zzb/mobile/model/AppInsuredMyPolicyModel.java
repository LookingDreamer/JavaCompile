package com.zzb.mobile.model;

public class AppInsuredMyPolicyModel {

	private String 	id; //id	
	
	private String carlicenseno;//车牌号
	
	private String ownername; //车主姓名
	
	private String createtime;  //创建时间
	private String processinstanceid;  //实例id
	
	private String webpagekey;//链接跳转状态 0 车辆信息，1车型信息，2供应商列表
	
	public String getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public String getWebpagekey() {
		return webpagekey;
	}

	public void setWebpagekey(String webpagekey) {
		this.webpagekey = webpagekey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCarlicenseno() {
		return carlicenseno;
	}

	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	

	
}
