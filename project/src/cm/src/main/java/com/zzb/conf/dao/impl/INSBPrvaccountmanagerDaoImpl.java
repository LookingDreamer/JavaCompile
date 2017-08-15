package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBPrvaccountmanagerDao;
import com.zzb.conf.entity.INSBPrvaccountmanager;

@Repository
public class INSBPrvaccountmanagerDaoImpl extends BaseDaoImpl<INSBPrvaccountmanager> implements
		INSBPrvaccountmanagerDao {
	@Override
	public int extendscount(Map<String, Object> map) {
		int i =0;
		List<Integer> lis = this.sqlSessionTemplate.selectList(this.getSqlName("extendscount"), map);
		for (Integer c : lis) {
			i++;
		}
		return i;
	}
	

	@Override
	public List<INSBPrvaccountmanager> extendsPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("extendsPage"), map);
	}
	@Override
	public List<INSBPrvaccountmanager> selectListPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListPage"), map);
	}

	@Override
	public int selectPageCountByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectPageCountByMap"), map);
	}

	@Override
	public List<INSBPrvaccountmanager> selectListPageNew(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListPageNew"), map);
	}
	
	@Override
	public List<String> selectListNew(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListNew"), map);
	}

	@Override
	public int selectPageCountByMapNew(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectPageCountByMapNew"), map);
	}

}