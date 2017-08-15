package com.zzb.mobile.model.usercenter;

/**
 * 用户机构信息
 */
public class UserCenterDept {
	/**
	 * 法人编码
	 */
    private String deptCode1;
	/**
	 * 法人名称
	 */
    private String deptName1;
	/**
	 * 分公司编码
	 */
    private String deptCode2;
	/**
	 * 分公司名称
	 */
    private String deptName2;
	/**
	 * 网点编码
	 */
    private String deptCode3;
	/**
	 * 网点名称
	 */
    private String deptName3;
	/**
	 * 团队编码
	 */
    private String deptCode4;
	/**
	 * 团队名称
	 */
    private String deptName4;
	/**
	 * 平台编码
	 */
    private String deptCode5;
	/**
	 * 平台名称
	 */
    private String deptName5;
	/**
	 * 机构类型01寿险02财险
	 */
    private String deptType;
	/**
	 * 代理人机构状态00授权01在职02二次入司03离职
	 */
    private String agentDeptState;
	/**
	 * 是否主法人机构 Y 是的   N 不是
	 */
	private String ismainFlag;
	
	public String getAgentDeptState() {
		return agentDeptState;
	}

	public void setAgentDeptState(String agentDeptState) {
		this.agentDeptState = agentDeptState;
	}

	public String getDeptCode1() {
		return deptCode1;
	}

	public void setDeptCode1(String deptCode1) {
		this.deptCode1 = deptCode1;
	}

	public String getDeptName1() {
		return deptName1;
	}

	public void setDeptName1(String deptName1) {
		this.deptName1 = deptName1;
	}

	public String getDeptCode2() {
		return deptCode2;
	}

	public void setDeptCode2(String deptCode2) {
		this.deptCode2 = deptCode2;
	}

	public String getDeptName2() {
		return deptName2;
	}

	public void setDeptName2(String deptName2) {
		this.deptName2 = deptName2;
	}

	public String getDeptCode3() {
		return deptCode3;
	}

	public void setDeptCode3(String deptCode3) {
		this.deptCode3 = deptCode3;
	}

	public String getDeptName3() {
		return deptName3;
	}

	public void setDeptName3(String deptName3) {
		this.deptName3 = deptName3;
	}

	public String getDeptCode4() {
		return deptCode4;
	}

	public void setDeptCode4(String deptCode4) {
		this.deptCode4 = deptCode4;
	}

	public String getDeptName4() {
		return deptName4;
	}

	public void setDeptName4(String deptName4) {
		this.deptName4 = deptName4;
	}

	public String getDeptCode5() {
		return deptCode5;
	}

	public void setDeptCode5(String deptCode5) {
		this.deptCode5 = deptCode5;
	}

	public String getDeptName5() {
		return deptName5;
	}

	public void setDeptName5(String deptName5) {
		this.deptName5 = deptName5;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getIsmainFlag() {
		return ismainFlag;
	}

	public void setIsmainFlag(String ismainFlag) {
		this.ismainFlag = ismainFlag;
	}
}
