package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBFlowerror;

public interface INSBFlowerrorDao extends BaseDao<INSBFlowerror> {

	public List<Map<Object, Object>> selectErrorListPaging(
			Map<String, Object> map);
	
	public long selectPagingCount(Map<String, Object> map);
	
	/**
	 * 获取所有的任务状态
	 * @return
	 */
	public List<INSBFlowerror> selectflowcode();

	public int selectMydataOneFfg(Map<String, String> map);

	public List<INSBFlowerror> selectOneTipGuizeshow(INSBFlowerror insbFlowerror);

	int selectMydataOneGZ(Map<String, String> map);
 
	int selectMydataOneRG(Map<String, String> map); 

	void updateMydataGZ(Map<String, String> map);

	void updateMydataRG(Map<String, String> map);
}