package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAutoconfig;
import com.zzb.conf.entity.INSBAutoconfigshow;

public interface INSBAutoconfigshowDao extends BaseDao<INSBAutoconfigshow> {
	public int deleteautoshow(Map<String, String> map);
	public int deleterepetitionautoshow(Map<String,String> map);
	/**
	 * 
	 * from autoconfig
	 * @param map
	 * @return
	 */
	public List<String> queryByProId(Map<String, Object> map);
	
	/**
	 * 
	 * from autoconfig
	 * @param para
	 * @return
	 */
	public INSBAutoconfig getQuoteType(Map<String, String> para);
	
	
	/**
	 * from autoconfig
	 * 
	 * 原mapper已经被注掉
	 * 
	 * @param agreementid
	 * @param conftype
	 * @return
	 */
	public int deleteByAgreementId(String agreementid,String conftype);
	
	/**
	 * from autoconfig
	 * 
	 * 
	 * @param contentid
	 * @return
	 */
	public int deletebyelfid(String contentid, String quoteType);
	
	
	
	
	/**
	 * from autoconfig
	 * 
	 * 原Mapper已经注掉
	 * 
	 * @param para
	 * @return
	 */
	public String selectByAgreementId(Map<String, String> para);
	
	
	/**
	 * from autoconfig
	 * 
	 * 原Mapper已经注掉
	 * 
	 * 根据代理人id和报价code值返回自动化配置方式表
	 * @param para
	 * @return
	 */
	public INSBAutoconfig getContracthbType(Map<String, String> para);
	
	
	/**
	 * from autoconfig
	 * 
	 * 通过供应商id和contendid查询所有的机构id
	 * @param param
	 * @return
	 */
	public List<Map<String,String>> selectComcodeBbyContenIdAndProviderId(Map<String,String> param);

	/**
	 * from autoconfig
	 * 
	 * 查询精灵EDI信息
	 * @param param
	 * @return
	 */
	public String selectContendIdByParam(Map<String,String> param);
	
	/**
	 * from autoconfig
	 * 
	 * 根据autoconfshow表的id删除能力配置列表
	 * @param showid
	 * @return
	 */
	public int deleteautobyshowid(Map<String, String> param);
	
	/**
	 * from autoconfig
	 * 
	 * @param id
	 * @param quotetype
	 */
	public void deleteautobyshowid(String id,String quotetype);
	
	/**
	 * 
	 *  from autoconfig
	 * 
	 * @param autoconfig
	 * @return
	 */
	public String insertReturnId(INSBAutoconfigshow autoconfig);
	
	
	/**
	 * 最多查出两条记录
	 * 
	 * @param param
	 * @return
	 */
	public List<INSBAutoconfigshow> selectDataByDeptIds4New(Map<String,Object> param);
	
	/**
	 * 根据平台Id查询该平台下拥有某一种能力的所有保险公司集合
	 * @param param
	 * @return
	 */
	public List<INSBAutoconfigshow> selectOneAbilityByDeptid(Map<String,String> param);
	
}