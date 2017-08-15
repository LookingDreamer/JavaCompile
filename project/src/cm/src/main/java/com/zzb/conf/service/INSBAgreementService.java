package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsbSaveTruleParms;

public interface INSBAgreementService extends BaseService<INSBAgreement> {

	public Long queryCountVo(INSBAgreement agreement);

	public List<INSBAgreement> queryListVo(INSBAgreement agreement);
	
	/**
	 * 查询所有有精灵
	 * @return
	 */
	public List<INSBElfconf> queryElfByProviderId(String providerid);
	
	/**
	 * 查询所有有EDI
	 * @return
	 */
	public List<Map<String,String>> queryEDIByProviderId(String providerid);
	
	/**
	 * 新增自动化报价配置
	 * 
	 * @param autoconfig
	 * @return 返回新增报价规则id
	 */
	public String saveAutoConfig(INSBAutoconfigshow autoconfig);
	/**
	 * 修改自动化报价配置
	 * 
	 * @param autoconfig
	 * @return 返回新增报价规则id
	 */
	public String updateAutoConfig(INSBAutoconfigshow autoconfig);
	
	/**
	 * 初始化 所有配置
	 * 
	 * 初始化选中配置
	 * 
	 * @param agreementId
	 * @return
	 */
	public Map<String,Object> initProcessAutoData(String agreementId);
	public String selectByAgreementId(String agreementId,String conftype);
	
	
	/**
	 * 初始化编辑页面数据
	 * 
	 * TODO  规则表和供应商表不同步 规则下拉列表暂时按固定值值查询
	 * 
	 * @param id
	 * @return
	 */
	public Map<String,Object> initEditePageData(String id);
	
	public Map<String,Object> initRuleByProviderId(String providerId);

	public List<INSBAgreement> queryListAgreement(Map<String,Object> map);

	public long queryCountAgreement(Map<String, Object> map);
	
	
	
	/**
	 * 
	 * 数据过滤
	 * @param parentcode
	 * @param deptId
	 * @return
	 */
	public List<Map<Object,Object>> queryDeptList(String parentcode,String deptId);
	
	public List<Map<String, String>> queryTreeListByAgr(String parentcode,String comtype,String deptId);
	
	/**
	 * 查询平台下的所有供应商
	 */
	public List<String>getAllProvider(String comcode);

	/**
	 * 根据deptid进行查询机构下的规则模糊查询
	 * @param deptid
	 * @param trulename
	 * @return
	 */
	public CommonModel getTrules(InsbSaveTruleParms model);

	public List<INSBAgreement> queryAgreementByComecode(Map<String,Object> map);

	Integer checkUnderwritestatus(String agreementid);

	/**
	 * 根据任务号&供应商编码获取协议
	 *
	 * @param taskId     任务号
	 * @param insComCode 供应商编码
	 * @return
	 */
	public INSBAgreement getAgreementByTaskIdAndInscomcode(String taskId, String insComCode);

}