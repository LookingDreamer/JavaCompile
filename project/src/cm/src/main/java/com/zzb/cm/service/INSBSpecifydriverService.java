package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.controller.vo.OtherInformationVO;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.entity.INSBSpecifydriver;

public interface INSBSpecifydriverService extends BaseService<INSBSpecifydriver> {
	/**
	 * 通过车辆信息id查询驾驶人信息
	 * @param carinfoId
	 * @return
	 */
	public List<Map<String, Object>> getSpecifydriversInfoByCarinfoId(String taskid, String inscomcode, String opeType);
	/**
	 * 修改其他信息弹出框提交修改处理
	 * @param otherInformation
	 * @return
	 */
	public String updateOtherInfo(OtherInformationVO otherInformation,INSBInvoiceinfo insbInvoiceinfo);
	/**
	 * 获取性别和驾驶证类型字典提供其他信息弹出框使用
	 */
	public Map<String, Object> getCodeOfGenderAndLicenseType();
}