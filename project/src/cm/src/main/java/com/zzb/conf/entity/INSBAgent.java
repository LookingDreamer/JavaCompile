package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.zzb.mobile.entity.ClientType;

import java.util.Date;

public class INSBAgent extends BaseEntity implements Identifiable {
		private static final long serialVersionUID = 1L;
	/**
	 * 序号 
	 */
	 private String rownum;
	

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 昵称
	 */
	private String nickname;
	
	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 工号
	 */
	private String jobnum;
	
	/**
	 * 临时工号
	 */
	private String tempjobnum;
	
	/**
	 * 工号类型
	 * 1：临时工号
	 * 2：正式工号
	 */
	private Integer jobnumtype;
	
	/**
	 * 正式工号关联虚拟类型
	 */
	private String jobnum4virtual;

	/**
	 * 手机账号
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 主营业务
	 */
	private String mainbusiness;

	/**
	 * 所属机构id
	 */
	private String deptid;
	
	
	/**
	 * 所属渠道
	 */
	private String channelid;
	/**
	 * 所属渠道名称
	 */
	private String channelname;
	
	/**

	 * 微信openid
	 */
	private String openid;

	/**
	 * 机构名称
	 */
	private String comname;
	/**
	 * 渠道来源
	 */
	private String purchaserchannel;

	/**
	 * 机构树
	 */
	private String treedept;

	/**
	 * 主法人机构网点id
	 */
	private String stationid;

	/**
	 * 代理人级别
	 */
	private Integer agentlevel;
	
	
	private String agentlevelstr;

	/**
	 * 代理人编码
	 */
	private String agentcode;

	/**
	 * 停用 、正常
	 */
	private Integer agentstatus;
	
	
	private String agentstatusstr;

	/**
	 * 用户种类
	 */
	private Integer agentkind;
	
	
	private String agentkindstr;
	
	

	/**
	 * 手机号码1
	 */
	private String mobile;

	/**
	 * 手机号码1配置
	 */
	private Integer mobilephone1use;

	/**
	 * 手机号码2
	 */
	private String mobile2;

	/**
	 * 手机号码2配置
	 */
	private Integer mobilephone2use;

	/**
	 * 座机电话
	 */
	private String telnum;

	/**
	 * 密码
	 */
	private String pwd;

	/**
	 * 是否测试账号
	 */
	private Integer istest;
	
	
	private String isteststr;
	
	

	/**
	 * 推荐人
	 */
	private String referrer;

	/**
	 * 权限包id
	 */
	private String setid;

	/**
	 * 资格证号
	 */
	private String cgfns;

	/**
	 * 执业证
	 */
	private String licenseno;

	/**
	 * 银行卡号
	 */
	private String bankcard;

	/**
	 * 用户来源
	 */
	private String usersource;

	/**
	 * 注册日期
	 */
	private Date registertime;
	
	
	private String registertimestr;

	/**
	 * 验证日期
	 */
	private Date testtime;
	
	
	private String testtimestr;

	/**
	 * 证件号码
	 */
	private String idno;

	/**
	 * 证件类别
	 */
	private String idnotype;

	/**
	 * 认证首次登陆
	 */
	private Date firstlogintime;
	
	
	private String firstlogintimestr;
	
	/**
	 * 认证状态
	 * `approvesstate` int(1) DEFAULT '1' COMMENT '认证状态  1-未认证  2-认证中  3-认证通过  4-认证失败',
	 */
	private Integer approvesstate;
	
	
	private String approvesstatestr;

	/**
	 * 所属城市
	 */
	private String livingcityid;

	/**
	 * 最后登陆
	 */
	private Date lsatlogintime;
	
	
	/**
	 * 非wif情况下上传图片质量 1：压缩 2：不压缩
	 */
	private Integer compression;
	
	/**
	 * 续保提醒时间
	 */
	private Integer renewaltime;
	
	
	/**
	 *  常用网点
	 */
	private String commonusebranch;
	
	
	private String lsatlogintimestr;
	/**
	 * 银行卡号所属银行
	 */
	private String belongs2bank;
	
	/**
	 * 居住地址
	 */
	private String address;
	/**
	 * 团队编码
	 */
	private String teamcode;
	/**
	 * 团队名称
	 */
	private String teamname;
	/**
	 * 所属平台编码
	 */
	private String platformcode;
	/**
	 * 所属平台名称
	 */
	private String platformname;
	/**
	 * 懒掌柜ID
	 */
	private String lzgid;
	/**
	 * 激活时间：后台认证通过的时间
	 */
	private Date activetime;
	/**
	 * 首单时间：后台首单通过支付到打单配送的时间
	 */
	private Date firstOderSuccesstime;
	/**
	 * 首单Taskid：后台首单通过支付taskid
	 */
	private String taskid;
	/**
	 * 备注
	 */
	private String noti;

	private String referrerid; // 推荐人uuid
	private String recommendtype; //推荐方式
	private String weixinheadphotourl; //用户微信头像URL

	private String pushChannel; //推广渠道
	private String pushWay; //推广方式

	private String clientType;

	public String getPushWay() {
		return pushWay;
	}

	public void setPushWay(String pushWay) {
		this.pushWay = pushWay;
	}

	public String getPushChannel() {
		return pushChannel;
	}

	public void setPushChannel(String pushChannel) {
		this.pushChannel = pushChannel;
	}

	public String getWeixinheadphotourl() {
		return weixinheadphotourl;
	}

	public void setWeixinheadphotourl(String weixinheadphotourl) {
		this.weixinheadphotourl = weixinheadphotourl;
	}

	public String getReferrerid() {
		return referrerid;
	}

	public void setReferrerid(String referrerid) {
		this.referrerid = referrerid;
	}

	public String getRecommendtype() {
		return recommendtype;
	}

	public void setRecommendtype(String recommendtype) {
		this.recommendtype = recommendtype;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String getNoti() {
		return noti;
	}

	@Override
	public void setNoti(String noti) {
		this.noti = noti;
	}

	public Date getActivetime() {
		return activetime;
	}

	public void setActivetime(Date activetime) {
		this.activetime = activetime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getJobnum() {
		return jobnum;
	}

	public String getFirstlogintimestr() {
		return firstlogintimestr;
	}

	public void setFirstlogintimestr(String firstlogintimestr) {
		this.firstlogintimestr = firstlogintimestr;
	}

	public String getLsatlogintimestr() {
		return lsatlogintimestr;
	}

	public void setLsatlogintimestr(String lsatlogintimestr) {
		this.lsatlogintimestr = lsatlogintimestr;
	}

	public String getRegistertimestr() {
		return registertimestr;
	}

	public void setRegistertimestr(String registertimestr) {
		this.registertimestr = registertimestr;
	}

	public String getTesttimestr() {
		return testtimestr;
	}

	public void setTesttimestr(String testtimestr) {
		this.testtimestr = testtimestr;
	}

	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMainbusiness() {
		return mainbusiness;
	}

	public void setMainbusiness(String mainbusiness) {
		this.mainbusiness = mainbusiness;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getTreedept() {
		return treedept;
	}

	public void setTreedept(String treedept) {
		this.treedept = treedept;
	}

	public String getStationid() {
		return stationid;
	}

	public String getAgentlevelstr() {
		return agentlevelstr;
	}

	public void setAgentlevelstr(String agentlevelstr) {
		this.agentlevelstr = agentlevelstr;
	}

	public String getAgentstatusstr() {
		return agentstatusstr;
	}

	public void setAgentstatusstr(String agentstatusstr) {
		this.agentstatusstr = agentstatusstr;
	}

	public String getAgentkindstr() {
		return agentkindstr;
	}

	public void setAgentkindstr(String agentkindstr) {
		this.agentkindstr = agentkindstr;
	}

	public String getIsteststr() {
		return isteststr;
	}

	public void setIsteststr(String isteststr) {
		this.isteststr = isteststr;
	}

	public String getApprovesstatestr() {
		return approvesstatestr;
	}

	public void setApprovesstatestr(String approvesstatestr) {
		this.approvesstatestr = approvesstatestr;
	}

	public void setStationid(String stationid) {
		this.stationid = stationid;
	}

	public Integer getAgentlevel() {
		return agentlevel;
	}

	public void setAgentlevel(Integer agentlevel) {
		this.agentlevel = agentlevel;
	}

	public String getAgentcode() {
		return agentcode;
	}

	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}

	public Integer getAgentstatus() {
		return agentstatus;
	}

	public void setAgentstatus(Integer agentstatus) {
		this.agentstatus = agentstatus;
	}

	public Integer getAgentkind() {
		return agentkind;
	}

	public void setAgentkind(Integer agentkind) {
		this.agentkind = agentkind;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getMobilephone1use() {
		return mobilephone1use;
	}

	public void setMobilephone1use(Integer mobilephone1use) {
		this.mobilephone1use = mobilephone1use;
	}

	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	public Integer getMobilephone2use() {
		return mobilephone2use;
	}

	public void setMobilephone2use(Integer mobilephone2use) {
		this.mobilephone2use = mobilephone2use;
	}

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getIstest() {
		return istest;
	}

	public void setIstest(Integer istest) {
		this.istest = istest;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getSetid() {
		return setid;
	}

	public void setSetid(String setid) {
		this.setid = setid;
	}

	public String getCgfns() {
		return cgfns;
	}

	public void setCgfns(String cgfns) {
		this.cgfns = cgfns;
	}

	public String getLicenseno() {
		return licenseno;
	}

	public void setLicenseno(String licenseno) {
		this.licenseno = licenseno;
	}

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getUsersource() {
		return usersource;
	}

	public void setUsersource(String usersource) {
		this.usersource = usersource;
	}

	public Date getRegistertime() {
		return registertime;
	}

	public void setRegistertime(Date registertime) {
		this.registertime = registertime;
	}

	public Date getTesttime() {
		return testtime;
	}

	public void setTesttime(Date testtime) {
		this.testtime = testtime;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getIdnotype() {
		return idnotype;
	}

	public void setIdnotype(String idnotype) {
		this.idnotype = idnotype;
	}

	public Date getFirstlogintime() {
		return firstlogintime;
	}

	public void setFirstlogintime(Date firstlogintime) {
		this.firstlogintime = firstlogintime;
	}

	public String getLivingcityid() {
		return livingcityid;
	}

	public void setLivingcityid(String livingcityid) {
		this.livingcityid = livingcityid;
	}

	public Date getLsatlogintime() {
		return lsatlogintime;
	}

	public void setLsatlogintime(Date lsatlogintime) {
		this.lsatlogintime = lsatlogintime;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	public String getPurchaserchannel() {
		return purchaserchannel;
	}

	public void setPurchaserchannel(String purchaserchannel) {
		this.purchaserchannel = purchaserchannel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Date getFirstOderSuccesstime() {
		return firstOderSuccesstime;
	}

	public void setFirstOderSuccesstime(Date firstOderSuccesstime) {
		this.firstOderSuccesstime = firstOderSuccesstime;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getApprovesstate() {
		return approvesstate;
	}

	public void setApprovesstate(Integer approvesstate) {
		this.approvesstate = approvesstate;
	}
	
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getJobnumtype() {
		return jobnumtype;
	}

	public void setJobnumtype(Integer jobnumtype) {
		this.jobnumtype = jobnumtype;
	}

	public String getTempjobnum() {
		return tempjobnum;
	}

	public void setTempjobnum(String tempjobnum) {
		this.tempjobnum = tempjobnum;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getChannelname() {
		return channelname;
	}


	
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public Integer getCompression() {
		return compression;
	}

	public void setCompression(Integer compression) {
		this.compression = compression;
	}

	public Integer getRenewaltime() {
		return renewaltime;
	}

	public void setRenewaltime(Integer renewaltime) {
		this.renewaltime = renewaltime;
	}

	public String getCommonusebranch() {
		return commonusebranch;
	}

	public void setCommonusebranch(String commonusebranch) {
		this.commonusebranch = commonusebranch;
	}
	
	
	public String getBelongs2bank() {
		return belongs2bank;
	}

	public void setBelongs2bank(String belongs2bank) {
		this.belongs2bank = belongs2bank;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTeamcode() {
		return teamcode;
	}

	public void setTeamcode(String teamcode) {
		this.teamcode = teamcode;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getPlatformcode() {
		return platformcode;
	}

	public void setPlatformcode(String platformcode) {
		this.platformcode = platformcode;
	}

	public String getPlatformname() {
		return platformname;
	}

	public void setPlatformname(String platformname) {
		this.platformname = platformname;
	}
	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}
	
	public String getLzgid() {
		return lzgid;
	}

	public void setLzgid(String lzgid) {
		this.lzgid = lzgid;
	}
	

	public String getJobnum4virtual() {
		return jobnum4virtual;
	}

	public void setJobnum4virtual(String jobnum4virtual) {
		this.jobnum4virtual = jobnum4virtual;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	@Override
	public String toString() {
		return "INSBAgent [rownum=" + rownum + ", name=" + name + ", nickname=" + nickname + ", sex=" + sex
				+ ", jobnum=" + jobnum + ", tempjobnum=" + tempjobnum + ", jobnumtype=" + jobnumtype
				+ ", jobnum4virtual=" + jobnum4virtual + ", phone=" + phone + ", email=" + email + ", mainbusiness="
				+ mainbusiness + ", deptid=" + deptid + ", channelid=" + channelid + ", channelname=" + channelname
				+ ", openid=" + openid + ", comname=" + comname + ", purchaserchannel=" + purchaserchannel
				+ ", treedept=" + treedept + ", stationid=" + stationid + ", agentlevel=" + agentlevel
				+ ", agentlevelstr=" + agentlevelstr + ", agentcode=" + agentcode + ", agentstatus=" + agentstatus
				+ ", agentstatusstr=" + agentstatusstr + ", agentkind=" + agentkind + ", agentkindstr=" + agentkindstr
				+ ", mobile=" + mobile + ", mobilephone1use=" + mobilephone1use + ", mobile2=" + mobile2
				+ ", mobilephone2use=" + mobilephone2use + ", telnum=" + telnum + ", pwd=" + pwd + ", istest=" + istest
				+ ", isteststr=" + isteststr + ", referrer=" + referrer + ", setid=" + setid + ", cgfns=" + cgfns
				+ ", licenseno=" + licenseno + ", bankcard=" + bankcard + ", usersource=" + usersource
				+ ", registertime=" + registertime + ", registertimestr=" + registertimestr + ", testtime=" + testtime
				+ ", testtimestr=" + testtimestr + ", idno=" + idno + ", idnotype=" + idnotype + ", firstlogintime="
				+ firstlogintime + ", firstlogintimestr=" + firstlogintimestr + ", approvesstate=" + approvesstate
				+ ", approvesstatestr=" + approvesstatestr + ", livingcityid=" + livingcityid + ", lsatlogintime="
				+ lsatlogintime + ", compression=" + compression + ", renewaltime=" + renewaltime + ", commonusebranch="
				+ commonusebranch + ", lsatlogintimestr=" + lsatlogintimestr + ", belongs2bank=" + belongs2bank
				+ ", address=" + address + ", teamcode=" + teamcode + ", teamname=" + teamname + ", platformcode="
				+ platformcode + ", platformname=" + platformname + ", lzgid=" + lzgid + ", activetime=" + activetime
				+ ", firstOderSuccesstime=" + firstOderSuccesstime + ", taskid=" + taskid + ", noti=" + noti
				+ ", referrerid=" + referrerid + ", recommendtype=" + recommendtype + ", weixinheadphotourl="
				+ weixinheadphotourl + ", pushChannel=" + pushChannel + ", pushWay=" + pushWay + "]";
	}

	

	

}