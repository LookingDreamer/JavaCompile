package com.zzb.mobile.model;

import java.util.List;

public class CheckInsuredConfigModel {
	/**
	 * true 规则校验有通过的，false，规则校验全部不通过
	 */
	private boolean flag;
	/**
	 * 规则校验返回信息
	 */
	private List<GuiZeCheckBean> guiZeCheckBeans;
	/**
	 * 先别校验
	 */
	private List<VerificationConfigBean> configBeans;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<GuiZeCheckBean> getGuiZeCheckBeans() {
		return guiZeCheckBeans;
	}

	public void setGuiZeCheckBeans(List<GuiZeCheckBean> guiZeCheckBeans) {
		this.guiZeCheckBeans = guiZeCheckBeans;
	}

	public List<VerificationConfigBean> getConfigBeans() {
		return configBeans;
	}

	public void setConfigBeans(List<VerificationConfigBean> configBeans) {
		this.configBeans = configBeans;
	}
	
	
}
