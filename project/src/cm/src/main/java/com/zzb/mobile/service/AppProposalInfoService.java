package com.zzb.mobile.service;

public interface AppProposalInfoService {

	/**
	 * 获取投保信息接口
	 * @param processInstanceId
     * @param inscomcode
	 * @return
	 */
	public String getProposalInfo(String processInstanceId,String inscomcode);
	
	/**
	 * 更新投保信息接口
	 * @param proposalInfo
	 * @return
	 */
	public String updateProposalInfo(String proposalInfo);

}
