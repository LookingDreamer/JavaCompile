package com.zzb.model;

import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.conf.entity.INSBPolicyitem;

public class CarDetailedModel {

	/**
	 * 保单
	 */
	private INSBPolicyitem insbPolicyitem;
	/**
	 * 车辆
	 */
	private INSBCarinfo carinfo;
	

	public INSBPolicyitem getInsbPolicyitem() {
		return insbPolicyitem;
	}

	public void setInsbPolicyitem(INSBPolicyitem insbPolicyitem) {
		this.insbPolicyitem = insbPolicyitem;
	}

	public INSBCarinfo getCarinfo() {
		return carinfo;
	}

	public void setCarinfo(INSBCarinfo carinfo) {
		this.carinfo = carinfo;
	}
	
	
}
