package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBAgreementdeptDao;
import com.zzb.conf.entity.INSBAgreementdept;

@Repository
public class INSBAgreementdeptDaoImpl extends BaseDaoImpl<INSBAgreementdept> implements
		INSBAgreementdeptDao {

	@Override
	public List<INSBAgreementdept> getINSBAgreementdept(Map<String, String> map) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"),map);
	}

	@Override
	public List<Map<String, String>> getPrvider(Map<String, String> map) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPrvid"),map);
	}

	@Override
	public Map<String,Object> getdeptname(Map<String, Object> map) {
		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getdeptname"),map);
	}

	@Override
	public int delByAgreeid(String agreeid) {
		return this.sqlSessionTemplate.delete(this.getSqlName("delByAgreeid"), agreeid);
	}

	@Override
	public List<INSBAgreementdept> queryByTaskIdAndPrvId(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryByTaskIdAndPrvId"), map);
	}

	/**
	 * 根据taskId 和 prvId获取平台机构信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDeptInfoByTaskIdAndPrvId(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getDeptInfoByTaskIdAndPrvId"), map);
	}
	
}
