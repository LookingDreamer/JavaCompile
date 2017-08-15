package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.io.Serializable;


public class INSBChannel extends BaseEntity implements Identifiable,Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 所属机构 
	 */
	private String deptid;

	/**
	 * 渠道内部编码
	 */
	private String channelinnercode;

	/**
	 * 是否使用渠道
	 */
	private String trychannel;

	/**
	 * 渠道编码
	 */
	private String channelcode;

	/**
	 * 渠道密钥
	 */
	private String channelsecret;

	/**
	 * 上级渠道
	 */
	private String upchannelcode;

	/**
	 * 渠道树路径
	 */
	private String treechannel;

	/**
	 * 渠道名称
	 */
	private String channelname;

	/**
	 * 渠道简称
	 */
	private String shortname;

	/**
	 * 渠道性质
	 */
	private String channelkind;

	/**
	 * 渠道类型
	 */
	private String channeltype;

	/**
	 * 渠道等级
	 */
	private String channelgrade;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 育成渠道代码
	 */
	private String rearchannelcode;

	/**
	 * 省code
	 */
	private String province;

	/**
	 * 市code
	 */
	private String city;

	/**
	 * 县code
	 */
	private String county;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 邮政编码
	 */
	private String zipcode;

	/**
	 * 电话号码
	 */
	private String phone;

	/**
	 * 传真
	 */
	private String fax;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 网页地址
	 */
	private String webaddress;

	/**
	 * 渠道负责人姓名
	 */
	private String satrapname;

	/**
	 * 渠道负责人代码
	 */
	private String satrapcode;

	/**
	 * 渠道负责人电话
	 */
	private String satrapphone;

	/**
	 * 子节点标记
	 */
	private String childflag;

	/**
	 * 排序
	 */
	private String orderflag;

	/**
	 * 节点层级
	 */
	private String treelevel;

	/**
	 * 出单工号
	 */
	private String jobnum;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 删除标志位
	 * 0为删除， 其他为未删除
	 */
	private String deleteflag;
	private String illustration; //渠道说明
	
//  寄件人单位名称
    private String senderchannel;
//  寄件人名字
    private String sendername;
//  寄件人地址
    private String senderaddress;
//  寄件人电话
    private String senderphone;
//  是否自定义
    private String isdefined;

	public String getIllustration() {
		return illustration;
	}

	public void setIllustration(String illustration) {
		this.illustration = illustration;
	}

	public String getDeleteflag() {
		return deleteflag;
	}

	public void setDeleteflag(String deleteflag) {
		this.deleteflag = deleteflag;
	}

	public String getChannelsecret() {
		return channelsecret;
	}

	public void setChannelsecret(String channelsecret) {
		this.channelsecret = channelsecret;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getChannelinnercode() {
		return channelinnercode;
	}

	public void setChannelinnercode(String channelinnercode) {
		this.channelinnercode = channelinnercode;
	}

	public String getTrychannel() {
		return trychannel;
	}

	public void setTrychannel(String trychannel) {
		this.trychannel = trychannel;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getUpchannelcode() {
		return upchannelcode;
	}

	public void setUpchannelcode(String upchannelcode) {
		this.upchannelcode = upchannelcode;
	}

	public String getTreechannel() {
		return treechannel;
	}

	public void setTreechannel(String treechannel) {
		this.treechannel = treechannel;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getChannelkind() {
		return channelkind;
	}

	public void setChannelkind(String channelkind) {
		this.channelkind = channelkind;
	}

	public String getChanneltype() {
		return channeltype;
	}

	public void setChanneltype(String channeltype) {
		this.channeltype = channeltype;
	}

	public String getChannelgrade() {
		return channelgrade;
	}

	public void setChannelgrade(String channelgrade) {
		this.channelgrade = channelgrade;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRearchannelcode() {
		return rearchannelcode;
	}

	public void setRearchannelcode(String rearchannelcode) {
		this.rearchannelcode = rearchannelcode;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebaddress() {
		return webaddress;
	}

	public void setWebaddress(String webaddress) {
		this.webaddress = webaddress;
	}

	public String getSatrapname() {
		return satrapname;
	}

	public void setSatrapname(String satrapname) {
		this.satrapname = satrapname;
	}

	public String getSatrapcode() {
		return satrapcode;
	}

	public void setSatrapcode(String satrapcode) {
		this.satrapcode = satrapcode;
	}

	public String getSatrapphone() {
		return satrapphone;
	}

	public void setSatrapphone(String satrapphone) {
		this.satrapphone = satrapphone;
	}

	public String getChildflag() {
		return childflag;
	}

	public void setChildflag(String childflag) {
		this.childflag = childflag;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

	public String getTreelevel() {
		return treelevel;
	}

	public void setTreelevel(String treelevel) {
		this.treelevel = treelevel;
	}

	public String getJobnum() {
		return jobnum;
	}

	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSenderchannel() {
		return senderchannel;
	}

	public void setSenderchannel(String senderchannel) {
		this.senderchannel = senderchannel;
	}

	public String getSendername() {
		return sendername;
	}

	public void setSendername(String sendername) {
		this.sendername = sendername;
	}

	public String getSenderaddress() {
		return senderaddress;
	}

	public void setSenderaddress(String senderaddress) {
		this.senderaddress = senderaddress;
	}

	public String getSenderphone() {
		return senderphone;
	}

	public void setSenderphone(String senderphone) {
		this.senderphone = senderphone;
	}

	public String getIsdefined() {
		return isdefined;
	}

	public void setIsdefined(String isdefined) {
		this.isdefined = isdefined;
	}
	
	

}