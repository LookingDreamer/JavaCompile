package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.zzb.mobile.entity.ClientType;

import java.util.Set;


public class INSBPaychannel extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/**
	 * 序号 
	 */
	 private String rownum;
	/**
	 * 支付通道名称
	 */
	private String paychannelname;

	/**
	 * 排序权重
	 */
	private String sort;

	/**
	 * 当前状态是否启用
	 */
	private String stateflag;

	/**
	 * 支付类型
	 */
	private String paytype;

	/**
	 * 支持终端
	 */
	private String supportterminal;

	/**
	 * 支付通道单笔支付限额
	 */
	private Double paychannellimit;

	/**
	 * 是否支持客户端付款
	 */
	private String clientpayflag;

	/**
	 * 通道描述
	 */
	private String channeldescribe;

	/**
	 * 费率优惠描述
	 */
	private String costratedescribe;

	/**
	 * 是否显示手续费
	 */
	private String showpoundageflag;

	/**
	 * 是否收取手续费
	 */
	private String chargepoundagefalg;

	/**
	 * 征收比率
	 */
	private String ratio;

	/**
	 * 手续费区分方法
	 */
	private String poundageflag;

	/**
	 * 手续费比率
	 */
	private String poundageratio;

	/**
	 * 单笔固定费
	 */
	private Double fixedfee;

	/**
	 * 信用卡手续费比率
	 */
	private String creditpoundageratio;

	/**
	 * 信用卡单笔固定费
	 */
	private Double creditfixedfee;

	/**
	 * 储蓄卡手续费比率
	 */
	private String cashpoundageratio;

	/**
	 * 储蓄卡单笔固定费
	 */
	private Double cashfixedfee;
	
	/**
	 * 修改时间
	 */
	private String updatetime;

	private String clienttypes;

	private boolean onlyedionlineunderwriting;

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getPaychannelname() {
		return paychannelname;
	}

	public void setPaychannelname(String paychannelname) {
		this.paychannelname = paychannelname;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getStateflag() {
		return stateflag;
	}

	public void setStateflag(String stateflag) {
		this.stateflag = stateflag;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getSupportterminal() {
		return supportterminal;
	}

	public void setSupportterminal(String supportterminal) {
		this.supportterminal = supportterminal;
	}

	public Double getPaychannellimit() {
		return paychannellimit;
	}

	public void setPaychannellimit(Double paychannellimit) {
		this.paychannellimit = paychannellimit;
	}

	public String getClientpayflag() {
		return clientpayflag;
	}

	public void setClientpayflag(String clientpayflag) {
		this.clientpayflag = clientpayflag;
	}

	public String getChanneldescribe() {
		return channeldescribe;
	}

	public void setChanneldescribe(String channeldescribe) {
		this.channeldescribe = channeldescribe;
	}

	public String getCostratedescribe() {
		return costratedescribe;
	}

	public void setCostratedescribe(String costratedescribe) {
		this.costratedescribe = costratedescribe;
	}

	public String getShowpoundageflag() {
		return showpoundageflag;
	}

	public void setShowpoundageflag(String showpoundageflag) {
		this.showpoundageflag = showpoundageflag;
	}

	public String getChargepoundagefalg() {
		return chargepoundagefalg;
	}

	public void setChargepoundagefalg(String chargepoundagefalg) {
		this.chargepoundagefalg = chargepoundagefalg;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getPoundageflag() {
		return poundageflag;
	}

	public void setPoundageflag(String poundageflag) {
		this.poundageflag = poundageflag;
	}

	public String getPoundageratio() {
		return poundageratio;
	}

	public void setPoundageratio(String poundageratio) {
		this.poundageratio = poundageratio;
	}


	public String getCreditpoundageratio() {
		return creditpoundageratio;
	}

	public void setCreditpoundageratio(String creditpoundageratio) {
		this.creditpoundageratio = creditpoundageratio;
	}

	
	public Double getFixedfee() {
		return fixedfee;
	}

	public void setFixedfee(Double fixedfee) {
		this.fixedfee = fixedfee;
	}

	public Double getCreditfixedfee() {
		return creditfixedfee;
	}

	public void setCreditfixedfee(Double creditfixedfee) {
		this.creditfixedfee = creditfixedfee;
	}

	public String getCashpoundageratio() {
		return cashpoundageratio;
	}

	public void setCashpoundageratio(String cashpoundageratio) {
		this.cashpoundageratio = cashpoundageratio;
	}

	public Double getCashfixedfee() {
		return cashfixedfee;
	}

	public void setCashfixedfee(Double cashfixedfee) {
		this.cashfixedfee = cashfixedfee;
	}
	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	public String getClienttypes() {
		return clienttypes;
	}

	public void setClienttypes(String clienttypes) {
		this.clienttypes = clienttypes;
	}

	public boolean isOnlyedionlineunderwriting() {
		return onlyedionlineunderwriting;
	}

	public void setOnlyedionlineunderwriting(boolean onlyedionlineunderwriting) {
		this.onlyedionlineunderwriting = onlyedionlineunderwriting;
	}

	@Override
	public String toString() {
		return "INSBPaychannel [paychannelname=" + paychannelname + ", sort="
				+ sort + ", stateflag=" + stateflag + ", paytype=" + paytype
				+ ", supportterminal=" + supportterminal + ", paychannellimit="
				+ paychannellimit + ", clientpayflag=" + clientpayflag
				+ ", channeldescribe=" + channeldescribe
				+ ", costratedescribe=" + costratedescribe
				+ ", showpoundageflag=" + showpoundageflag
				+ ", chargepoundagefalg=" + chargepoundagefalg + ", ratio="
				+ ratio + ", poundageflag=" + poundageflag + ", poundageratio="
				+ poundageratio + ", fixedfee=" + fixedfee
				+ ", creditpoundageratio=" + creditpoundageratio
				+ ", creditfixedfee=" + creditfixedfee + ", cashpoundageratio="
				+ cashpoundageratio + ", cashfixedfee=" + cashfixedfee
				+ ", updatetime=" + updatetime + ", rownum=" + rownum + "]";
	}

	
	
	

}