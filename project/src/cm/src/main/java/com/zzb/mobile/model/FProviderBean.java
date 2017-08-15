package com.zzb.mobile.model;

import java.util.ArrayList;
import java.util.List;

import com.zzb.conf.entity.INSBProvider;

public class FProviderBean {
	private INSBProvider fProvider;
	private List<SAProviderBean> saProviderBeans =new ArrayList<SAProviderBean>();
	public INSBProvider getfProvider() {
		return fProvider;
	}
	public void setfProvider(INSBProvider fProvider) {
		this.fProvider = fProvider;
	}
	public List<SAProviderBean> getSaProviderBeans() {
		return saProviderBeans;
	}
	public void setSaProviderBeans(List<SAProviderBean> saProviderBeans) {
		this.saProviderBeans = saProviderBeans;
	}
	
}
