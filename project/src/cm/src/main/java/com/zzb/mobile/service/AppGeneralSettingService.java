package com.zzb.mobile.service;

import com.zzb.mobile.model.CommonModel;

public interface AppGeneralSettingService {
	/**
	 * 修改消息接受状态
	 * @param agentJobnum 代理人工号
	 * @param message 消息接受状态：接受 1 \ 不接受0
	 * @return 
	 */
	public CommonModel updateMsgAcceptingStatus(String agentJobnum,String message);
	/**
	 * 修改非WIFI情况下图片上传质量接口
	 * @param agentId 代理人工号
	 * @param imageCompression 图片上传质量：压缩 1 \ 不压缩2
	 * @return
	 */
	public CommonModel updatePictureUploadQuality(String agentJobnum,String imageCompression);
	/**
	 * 获取常用出单网点
	 * @param agentJobnum 代理人工号
	 * @return
	 */
	public CommonModel getCommonOutDept(String agentJobnum);
	/**
	 * 
	 * @param phone 手机号
	 * @param validatecode 验证码
	 * @param agentJobnum 代理人工号
	 * @return
	 */
	public CommonModel updateAgentPhone(String phone, String validatecode, String agentJobnum);
	/**
	 * 
	 * @param phone 手机号
	 * @param agentJobnum 代理人工号
	 * @return
	 */
	public CommonModel sendValidateCode(String phone, String agentJobnum);
	/**
	 * 获取所有省
	 * @return 
	 */
	public CommonModel getAllProvince();
	
	/**
	 * 获取所有协议关联网点所在的市
	 * @return
	 */
	public CommonModel getAllCity(String parentcode);
	
	/**
	 * 获取所有协议关联网点所在的区
	 * @return
	 */
	public CommonModel getAllCountry(String parentcode);
	
	public CommonModel getAllProvincefromSD();
	/**
	 * 根据代码获取区域
	 * @param code 代码
	 * @return
	 */
	public CommonModel getRegionByCode(String code);
	/**
	 * 根据上级代码获取所有下属区域
	 * @param parentcode 上级代码
	 * @return 
	 */
	public CommonModel getRegionByParentCode(String parentcode);
	/**
	 * 通用设置查询   
	 * @param jobnum 代理人工号
	 * @return 非WIFI情况下图片上传质量 压缩 1 \ 不压缩2 ,消息接受状态  接受 1 \ 不接受0 ,当前常用出单网点
	 */
	public CommonModel queryGeneralSetting(String jobnum);
	/**
	 * 查询代理人所在机构的工作时间
	 * @param agentJobnum
	 * @return
	 */
	public CommonModel queryWorkTime(String agentJobnum);
	
	/**
	 * 待支付配送-获取所有地市
	 * @param parentcode
	 * @return
	 */
	public CommonModel getAllCityForPS(String parentcode);
	
	/**
	 * 待支付配送-获取所有区县
	 * @param parentcode
	 * @return
	 */
	public CommonModel getAllCountryForPS(String parentcode);
	public CommonModel updateRegioncode(String jobNum, String provinceCode,
			String cityCode, String countyCode);

	public CommonModel getRegisterProvince();
}
