package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBPrvaccountmanager;

public interface INSBPrvaccountmanagerDao extends BaseDao<INSBPrvaccountmanager> {
	
	/**
	 * 查询分页
	 * @param map
	 * @return
	 */
	public List<INSBPrvaccountmanager>  selectListPage(Map<String, Object> map);
	
	
	
	/**
	 * 新需求 0322
	 * @param map
	 * @return
	 */
	public List<INSBPrvaccountmanager>  selectListPageNew(Map<String, Object> map);
	
	/**
	 * 查询分页条数
	 * @param map
	 * @return
	 */
	public int  selectPageCountByMap(Map<String, Object> map);
	
	
	/**
	 * 新需求 0322
	 * @param map
	 * @return
	 */
	public int  selectPageCountByMapNew(Map<String, Object> map);
	
	/**
	 * 拿到所有上级结点id
	 * 
	 * @param map
	 * @return
	 */
	public List<String> selectListNew(Map<String, Object> map);


	int extendscount(Map<String, Object> map);


	List<INSBPrvaccountmanager> extendsPage(Map<String, Object> map);

}