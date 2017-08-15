package com.zzb.extra.controller.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author shiguiwu
 * @date 2017年4月21日
 */
public class TaxrateVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 渠道id
	 */
	private String channelid;
	
	/**
	 * 税率状态
	 */
	private String status;
	/**
	 * 关键字
	 */
	private String keyword;
	
	/**
	 * 渠道名称
	 */
	private String channelname;
	
	/**
	 * 渠道编号
	 */
	private String channelcode;
	/**
	 * 创建时间
	 */
	private  String createtime;
	
	
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	public String getChannelcode() {
		return channelcode;
	}
	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	 
	

}
