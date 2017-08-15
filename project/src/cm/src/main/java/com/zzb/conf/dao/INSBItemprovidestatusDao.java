package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBItemprovidestatus;

/**
 * 供应商限定状态表
 * 
 * @author Administrator
 *
 */
public interface INSBItemprovidestatusDao extends BaseDao<INSBItemprovidestatus> {

	/**
	 * 得到当前功能包所有供应商信息
	 * 
	 * @param setId
	 * @return
	 */
	public List<INSBItemprovidestatus> selectListBySetId(String setId);
	
	
	/**
	 * 通过权限包id得到所有供应商id
	 * @param setId
	 * @return
	 */
	public List<String> selectProviderIdBySetId(String setId);
	
	
	/**
	 * 按逻辑主键删除
	 * 
	 * @param ids
	 * @return
	 */
	public int deleteBySetIdproviderId(Map<String, String> ids);
	

	/**
	 * 删除功能包对应供应商信息（删除功能包做级联删除）
	 * 
	 * @param setId
	 */
	public void deleteBySetId(String pId);
	
}