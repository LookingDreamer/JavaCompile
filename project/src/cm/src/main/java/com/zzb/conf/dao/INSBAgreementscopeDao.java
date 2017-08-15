package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgreementscope;

public interface INSBAgreementscopeDao extends BaseDao<INSBAgreementscope> {
	public List<INSBAgreementscope> selectAdrAndDeptName(INSBAgreementscope query);
	/**
	 * 查询所选区域下的所有网点
	 * @param insbAgreementscope
	 * @return
	 */
	public List<String> selectAreaDeptid(String city);
	
	/**
	 * 通过协议id得到所有的组织机构
	 * 
	 * @param agreementid
	 * @return
	 */
	public List<String> selectDeptIdByAgreementId(String agreementid);
	
	
	/**
	 * 通过区域查找对应协议
	 * 
	 * @param deptid
	 * @return
	 */
	public List<String> selectAgreementIdByDeptId(String deptid);
	/**
	 * 根据城市id关联网点
	 * 
	 * @param map
	 * @return
	 */
	public List<INSBAgreementscope> insertscope(Map<String, Object> map);
	public void insertscopes(Map<String, Object> map);
	/**
	 * 根据协议id和城市code删除关联网点
	 * 
	 * @param map
	 * @return
	 */
	public void deletebyagreementid(String agreementid,String city);
	
}