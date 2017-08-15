package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgreement;

public interface INSBAgreementDao extends BaseDao<INSBAgreement> {

	public Long queryCountVo(INSBAgreement agreement);

	public List<INSBAgreement> queryListVo(INSBAgreement agreement);

	/**
	 * 通过组织机构查询供应商
	 * 
	 * @param deptid
	 * @return
	 */
	public List<String> selectProviderIdByDeptId(String deptid);

	/**
	 * 根据供应商id获取协议
	 * 
	 * @param providerId
	 * @return
	 */
	public List<String> selectAgreementIdByproviderId(String providerId);

	/**
	 * 通过组织机构得到所有的协议id
	 * 
	 * @param deptid
	 * @return
	 */
	public List<String> selectAgreementIdByDeptId(String deptid);

	/**
	 * 通过协议id得到供应商id
	 * 
	 * @param id
	 * @return
	 */
	public String selectProviderIdByAgreementId(String id);

	/**
	 * 通过供应商id得到传统规则列表
	 * 
	 * @param providerid 供应商ID
	 * @param deptid 网点ID
	 * @return
	 */
	public List<String> selectAgreementtruleListByProviderid(String providerid,
			String deptid);
	/**
	 * 查询上、本、下级协议
	 * @param comcode
	 * @return 
	 */
	public List<INSBAgreement> queryAgreement(Map<String,Object> map);
	
	/**
	 * 查询当前机构下所有协议
	 * 
	 * @param deptids
	 * @return
	 */
	public List<INSBAgreement> selectAllSubAgreement(List<String> deptids);

	/**
	 * 获取机构平台下的供应商
	 */
	public List<String>getAgreementProvider(String comcode);

	/**
	 * 根据出单网点获取协议
	 * @param map{deptid5}
	 * @return
     */
	public List<INSBAgreement> selectByOutorderdept(Map<String,Object> map);

	public List<INSBAgreement> queryAgreementByComecode(Map<String,Object> map);

}