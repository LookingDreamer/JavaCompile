package com.zzb.conf.controller.vo;
/**
 * 导出权限包异常信息
 * @author lining
 *
 */
public class ExportPermissionInfoVo {
	private int id;
	private String jobnum; // 工号
	private String mobile; // 手机号
	private int result; // 关联结果 1成功 0失败
	private String reason; // 原因
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJobnum() {
		return jobnum;
	}
	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
