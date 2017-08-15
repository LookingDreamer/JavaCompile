package com.zzb.conf.service;

import com.cninsure.system.entity.INSCUser;
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBAutoconfigshowQueryModel;

public interface INSBAutoconfigshowService extends BaseService<INSBAutoconfigshow> {
	public int deleteautoshow(String proid,String deptid);
	
	public int deleterepetitionautoshow(String parentcodes, String proid, String deptid);
	
	
	
	
	/**
	 * 得到精灵edi 网址
	 * 
	 * @param deptId
	 * @return
	 */
	public Map<String,Object>  getElfEdiByNearestDept(INSBAutoconfigshowQueryModel queryModel);
	
	/**
	 * 
	 * 报价方式自动人工
	 * @param dataMap
	 * @return
	 */
	public List<INSBAutoconfigshow> autoOrArtificial(INSBAutoconfigshowQueryModel queryModel);
	
	/**
	 * 得到本级及上级deptid集合
	 * 
	 * @param DeptId
	 * @return
	 */
	public List<String> getParentDeptIds4Show(String DeptId);
	
	/**
	 * 根据供应商id查询自动化配置信息
	 * @return
	 */
	public List<String> queryByProId(Map<String, Object> map);

	int deleteElfAutoConfigShowByElfId(String id);

	int deleteAutoByShowId(String showid, String parentcodes, String deptid);

	Map<String,String> saveEdiConfig(String type, String showid, String providerId, String deptId, String codevalue, String conftype, INSCUser user);

	void deleteAutoByShowId(String id, String quotetype);
	
	/**
	 * 根据平台Id获得该平台下拥有某一种能力(如平台)的所有保险公司
	 * @param deptId
	 * @return
	 */
	public List<INSBAutoconfigshow> getOneAbilityByDeptId(String deptId, String conftypeStr);
	
	/**
	 * 根据[平台Id和报价类型(EDI/精灵)]查询该平台下拥有某一种能力的所有保险公司集合
	 * @param param
	 * @return
	 */
	public List<INSBAutoconfigshow> getOneAbilityByDeptIdQuotype(String deptId, String conftypeStr, String quotype);
}