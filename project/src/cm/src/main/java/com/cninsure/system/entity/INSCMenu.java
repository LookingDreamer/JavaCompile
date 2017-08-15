package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

@SuppressWarnings("serial")
public class INSCMenu extends BaseEntity implements Identifiable {

	private String nodecode;
	private String parentnodecode;
	private String nodelevel;
	private String nodename;
	private String childflag;
	private String activeurl;
	private String iconurl;
	private Integer orderflag;
	private String status;
	public String getNodecode() {
		return nodecode;
	}
	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}
	public String getParentnodecode() {
		return parentnodecode;
	}
	public void setParentnodecode(String parentnodecode) {
		this.parentnodecode = parentnodecode;
	}
	public String getNodelevel() {
		return nodelevel;
	}
	public void setNodelevel(String nodelevel) {
		this.nodelevel = nodelevel;
	}
	public String getNodename() {
		return nodename;
	}
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}
	public String getChildflag() {
		return childflag;
	}
	public void setChildflag(String childflag) {
		this.childflag = childflag;
	}
	public String getActiveurl() {
		return activeurl;
	}
	public void setActiveurl(String activeurl) {
		this.activeurl = activeurl;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public Integer getOrderflag() {
		return orderflag;
	}
	public void setOrderflag(Integer orderflag) {
		this.orderflag = orderflag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "INSCMenu [nodecode=" + nodecode + ", parentnodecode="
				+ parentnodecode + ", nodelevel=" + nodelevel + ", nodename="
				+ nodename + ", childflag=" + childflag + ", activeurl="
				+ activeurl + ", iconurl=" + iconurl + ", orderflag="
				+ orderflag + ", status=" + status + "]";
	}

	
}
