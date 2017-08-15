package com.zzb.mobile.model;

public class SearchProviderModel {
	/**是否检查数据完成，1 检查  0 不检查**/
	public static final String INSPECT_0 = "0";
	/**是否检查数据完成，1 检查  0 不检查**/
	public static final String INSPECT_1 = "1";
	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 渠道 0 传统 1 网销
	 */
	private String channel;
	/**
	 * 代理人id
	 */
	private String agentid;
	/**
	 * 区域省code
	 */
	private String province;
	/**
	 * 市code
	 */
	private String city;

    /**
     * 供应商id（只查询指定供应商下的协议）
     * @return
     */
    private String prvid;
    /**
     * 是否检查数据完成，1 检查  0 不检查
     */
    private String inspect;
	
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getAgentid() {
		return agentid;
	}
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

    public String getPrvid() {
        return prvid;
    }

    public void setPrvid(String prvid) {
        this.prvid = prvid;
    }
	public String getInspect() {
		return inspect;
	}
	public void setInspect(String inspect) {
		this.inspect = inspect;
	}
    
}
