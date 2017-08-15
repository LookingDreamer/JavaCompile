package com.zzb.cm.controller.vo;

public class UnderwritingResultVO {
	
	//子流程id
	private String subInstanceId;
	
	//核保回写处理结果 success/fail
	private String result;

	public String getSubInstanceId() {
		return subInstanceId;
	}

	public void setSubInstanceId(String subInstanceId) {
		this.subInstanceId = subInstanceId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "UnderwritingResultVO [subInstanceId=" + subInstanceId
				+ ", result=" + result + "]";
	}
	
}
