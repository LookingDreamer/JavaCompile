package com.zzb.cm.controller.vo;

import com.zzb.cm.entity.INSBInsuresupplyparam;

import java.util.List;

/*
 * MediumPaymentController 接口中传递的参数，
 * 
 */
public class SupplyParamVo {
	/*
	 * 任务id
	 */
	private String taskid;

	private String inscomcode;

    private List<INSBInsuresupplyparam> supplyparam;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public List<INSBInsuresupplyparam> getSupplyparam() {
		return supplyparam;
	}

	public void setSupplyparam(List<INSBInsuresupplyparam> supplyparam) {
		this.supplyparam = supplyparam;
	}
}
