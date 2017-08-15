package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBPrvaccountkey;

public interface INSBPrvaccountkeyDao extends BaseDao<INSBPrvaccountkey> {
	/**
	 * 查询分页
	 * @param map
	 * @return
	 */
	public List<INSBPrvaccountkey>  selectListPage(Map<String, Object> map);
	
	
	/**
	 * 新需求 0323
	 * @param map
	 * @return
	 */
	public List<INSBPrvaccountkey>  selectListPageNew(Map<String, Object> map);
	
	/**
	 * 查询分页条数
	 * @param map
	 * @return
	 */
	public int  selectPageCountByMap(Map<String, Object> map);
	
	/**
	 * 新需求0323
	 * @param map
	 * @return
	 */
	public int  selectPageCountByMapNew(Map<String, Object> map);

	public List<INSBPrvaccountkey> extendsPage(Map<String, Object> map);
	

	public int extendscount(Map<String, Object> map);
	
	public List<Map<String, Object>>  selectConfigInfo(Map<String, Object> map);
}