package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgreementdept;

public interface INSBAgreementdeptDao extends BaseDao<INSBAgreementdept> {

	public List<INSBAgreementdept> getINSBAgreementdept(Map<String,String> map);
	
	public List<Map<String,String>> getPrvider(Map<String,String> map);
	
	public Map<String,Object> getdeptname(Map<String,Object> map);
	
	public int delByAgreeid(String agreeid);
	
	public List<INSBAgreementdept> queryByTaskIdAndPrvId(Map<String, String> map);

	/**
	 * 根据taskId 和 prvId获取平台机构信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeptInfoByTaskIdAndPrvId(Map<String, Object> map);
	
}