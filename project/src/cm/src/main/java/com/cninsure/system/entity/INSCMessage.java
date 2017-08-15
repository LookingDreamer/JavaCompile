package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSCMessage extends BaseEntity implements Identifiable{
	  private String id;
	  private String sender;
	  private String receiver;
	  private String msgtitle;
	  private String msgcontent;
	  private String sendtime;
	  private String readtime;
	  private Integer status;
	  private String linkurl;
	  private Integer state;
	  
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMsgtitle() {
		return msgtitle;
	}
	public void setMsgtitle(String msgtitle) {
		this.msgtitle = msgtitle;
	}
	public String getMsgcontent() {
		return msgcontent;
	}
	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getReadtime() {
		return readtime;
	}
	public void setReadtime(String readtime) {
		this.readtime = readtime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "INSCMessage [id=" + id + ", sender=" + sender + ", receiver="
				+ receiver + ", msgtitle=" + msgtitle + ", msgcontent="
				+ msgcontent + ", sendtime=" + sendtime + ", readtime="
				+ readtime + ", status=" + status + ", linkurl=" + linkurl
				+ ", state=" + state + "]";
	}
	
	  
}
