package com.zzb.conf.controller.vo;

import java.io.Serializable;

import com.zzb.conf.entity.INSBPaychannelmanager;

/**
 * 支付通道管理页面 vo
 * 
 * @author hxx
 *
 */
public class PaychannelmanagerVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 移动快刷
	 */
	private INSBPaychannelmanager pcm1;
	
	/**
	 * 翼支付
	 */
	private INSBPaychannelmanager pcm2;
	
	/**
	 * 易联支付
	 */
	private INSBPaychannelmanager pcm3;
	
	/**
	 * 柜台POS支付
	 */
	private INSBPaychannelmanager pcm4;
	
	/**
	 * 转账
	 */
	private INSBPaychannelmanager pcm5;
	/**
	 * 平安网页支付
	 */
	private INSBPaychannelmanager pcm6;
	public INSBPaychannelmanager getPcm1() {
		return pcm1;
	}
	public void setPcm1(INSBPaychannelmanager pcm1) {
		this.pcm1 = pcm1;
	}
	public INSBPaychannelmanager getPcm2() {
		return pcm2;
	}
	public void setPcm2(INSBPaychannelmanager pcm2) {
		this.pcm2 = pcm2;
	}
	public INSBPaychannelmanager getPcm3() {
		return pcm3;
	}
	public void setPcm3(INSBPaychannelmanager pcm3) {
		this.pcm3 = pcm3;
	}
	public INSBPaychannelmanager getPcm4() {
		return pcm4;
	}
	public void setPcm4(INSBPaychannelmanager pcm4) {
		this.pcm4 = pcm4;
	}
	public INSBPaychannelmanager getPcm5() {
		return pcm5;
	}
	public void setPcm5(INSBPaychannelmanager pcm5) {
		this.pcm5 = pcm5;
	}
	public INSBPaychannelmanager getPcm6() {
		return pcm6;
	}
	public void setPcm6(INSBPaychannelmanager pcm6) {
		this.pcm6 = pcm6;
	}
	

}
