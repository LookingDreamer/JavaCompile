package com.zzb.cm.Interface.entity.car.model; /**
 * 
 */

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Api访问日志，供后续分析用
 * @author austinChen
 * created at 2015年6月12日 下午2:00:34
 */
public class VisitLog {
	
	private ChannelInfo channel;
	private Date created;
	private String action;
	private String path;
	private Integer code;
	private String msg;
	private String remark;
	private BigDecimal fee;
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ChannelInfo getChannel() {
		return channel;
	}
	public void setChannel(ChannelInfo channel) {
		this.channel = channel;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	
	
	

}
