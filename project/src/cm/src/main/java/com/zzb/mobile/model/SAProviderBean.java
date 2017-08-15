package com.zzb.mobile.model;

import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBProvider;

public class SAProviderBean {
	private INSBProvider sProvider;
	private INSBAgreement agreement;
	public INSBProvider getsProvider() {
		return sProvider;
	}
	public void setsProvider(INSBProvider sProvider) {
		this.sProvider = sProvider;
	}
	public INSBAgreement getAgreement() {
		return agreement;
	}
	public void setAgreement(INSBAgreement agreement) {
		this.agreement = agreement;
	}
	
}
