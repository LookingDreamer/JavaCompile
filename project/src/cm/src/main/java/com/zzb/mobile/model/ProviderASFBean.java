package com.zzb.mobile.model;

import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBProvider;

public class ProviderASFBean {
	private INSBProvider fProvider;
	private INSBProvider sProvider;
	private INSBAgreement agreement;
	public INSBAgreement getAgreement() {
		return agreement;
	}
	public void setAgreement(INSBAgreement agreement) {
		this.agreement = agreement;
	}
	public INSBProvider getsProvider() {
		return sProvider;
	}
	public void setsProvider(INSBProvider sProvider) {
		this.sProvider = sProvider;
	}
	public INSBProvider getfProvider() {
		return fProvider;
	}
	public void setfProvider(INSBProvider fProvider) {
		this.fProvider = fProvider;
	}
	
	
}

