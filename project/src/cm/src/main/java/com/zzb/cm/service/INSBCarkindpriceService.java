package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.controller.vo.CarInsConfigVo;
import com.zzb.cm.controller.vo.CarInsuranceConfigVo;
import com.zzb.cm.controller.vo.SpecialRiskkindCfgVo;
import com.zzb.cm.entity.INSBCarkindprice;

public interface INSBCarkindpriceService extends BaseService<INSBCarkindprice> {

	/**
	 * 修改保险配置信息
	 */
	public String editInsuranceConfig(CarInsuranceConfigVo insConfigVo);
	
	/**
	 * 通过保险公司代码和任务id查询已选择的险别配置信息
	 */
	public Map<String,Object> getCarInsConfigByInscomcode(String inscomcode, String processInstanceId);
	
	/**
	 * 通过保险公司代码和任务id查询保费
	 */
	public Map<String,Object> getPremiumInfo(String taskid, String inscomcode);
	public List<Map<String,String>>selectAllriskkind(String inscomcode);
	
	/**
	 * 通过保险公司代码和任务id查询保费
	 */
	public double getTotalProductAmount(String taskid, String inscomcode);
	
	/**
	 * 查询特殊险别下的配置信息，如新增设备损失险
	 */
	public List<Map<String, Object>> getSpecialRiskkindcfg(String mInstanceid, String inscomcode, String riskkindcode);
	
	/**
	 * 修改特殊险别下的配置信息，如新增设备损失险
	 */
	public Map<String, String> editSpecialRiskkindcfg(SpecialRiskkindCfgVo specialRiskkindCfg);

	
}